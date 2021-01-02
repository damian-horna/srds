package cassdemo;

import cassdemo.backend.BackendSession;
import cassdemo.domain.Flight;
import cassdemo.domain.Hotel;
import cassdemo.domain.PlaneSeat;
import cassdemo.domain.Room;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DbService {
    BackendSession backendSession;
    private static final Logger logger = LoggerFactory.getLogger(DbService.class);

    public DbService(BackendSession backendSession) {
        this.backendSession = backendSession;
    }

    public void addFlight(Flight f) {
        // Insert flight
        PreparedStatement INSERT_INTO_FLIGHTS = backendSession
                .session
                .prepare("INSERT INTO flights (id, plane_id, start_city, target_city) VALUES (?, ?, ?, ?);");
        BoundStatement bs1 = new BoundStatement(INSERT_INTO_FLIGHTS);
        bs1.bind(f.id, f.plane.id, f.startCity, f.targetCity);
        ResultSet rs1 = execute(bs1);

        // Insert seats
        PreparedStatement INSERT_INTO_SEATS = backendSession
                .session
                .prepare("INSERT INTO available_plane_seats_by_flight (plane_id, flight_id, seat_id, available) VALUES (?,?,?,?);");
        for (Integer row : f.plane.seats.keySet()) {
            for (PlaneSeat s : f.plane.seats.get(row)) {
                BoundStatement bs2 = new BoundStatement(INSERT_INTO_SEATS);
                bs2.bind(f.plane.id, f.id, s.id, s.available);
                ResultSet rs2 = execute(bs2);
            }
        }
    }

    private ResultSet execute(BoundStatement bs1) {
        ResultSet rs = null;
        try {
            rs = backendSession.session.execute(bs1);
        } catch (Exception e) {
            System.out.println("Error while executing statement " + e.getMessage());
        }
        return rs;
    }

    public void addHotel(Hotel h) {
        // Insert hotel
        PreparedStatement INSERT_INTO_HOTELS = backendSession
                .session
                .prepare("INSERT INTO hotels (id, city) VALUES (?, ?);");
        BoundStatement bs1 = new BoundStatement(INSERT_INTO_HOTELS);
        bs1.bind(h.id, h.city);
        ResultSet rs1 = execute(bs1);

        // Insert rooms
        PreparedStatement INSERT_INTO_ROOMS = backendSession
                .session
                .prepare("INSERT INTO available_hotel_rooms_by_city_and_capacity (room_id, city, capacity, hotel_id, available) VALUES (?,?,?,?,?);");
        for (Room r: h.rooms){
            BoundStatement bs2 = new BoundStatement(INSERT_INTO_ROOMS);
            bs2.bind(r.id, h.city, r.capacity, h.id, r.available);
            ResultSet rs2 = execute(bs2);
        }
    }
}
