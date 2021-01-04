package cassdemo;

import cassdemo.backend.BackendSession;
import cassdemo.domain.*;
import com.datastax.driver.core.BoundStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.ResultSet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

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
                .prepare("UPDATE available_plane_seats_by_flight SET available=available+1 where flight_id=? and seat_id=?;");
        for (Integer row : f.plane.seats.keySet()) {
            for (PlaneSeat s : f.plane.seats.get(row)) {
                BoundStatement bs2 = new BoundStatement(INSERT_INTO_SEATS);
                bs2.bind(f.id, s.id);
                ResultSet rs2 = execute(bs2);
            }
        }
    }

    public void addHotel(Hotel h) {
        // Insert hotel
        PreparedStatement INSERT_INTO_HOTELS = backendSession
                .session
                .prepare("INSERT INTO hotels (id, city) VALUES (?, ?);");
        BoundStatement bs1 = new BoundStatement(INSERT_INTO_HOTELS);
        bs1.bind(h.id, h.city);
        execute(bs1);

        // Insert rooms
        PreparedStatement INSERT_INTO_ROOMS = backendSession
                .session
                .prepare("UPDATE available_hotel_rooms_by_capacity SET available=available+1 WHERE room_id=? and capacity=? and hotel_id=?;");
        for (Room r : h.rooms) {
            BoundStatement bs2 = new BoundStatement(INSERT_INTO_ROOMS);
            bs2.bind(r.id, r.capacity, h.id);
            execute(bs2);
        }
    }

    public List<Flight> selectAllFlights() {
        PreparedStatement SELECT_ALL_FLIGHTS = backendSession
                .session
                .prepare("SELECT * from flights;");
        BoundStatement bs1 = new BoundStatement(SELECT_ALL_FLIGHTS);
        ResultSet rs1 = execute(bs1);

        List<Flight> flightsList = new ArrayList<>();
        rs1.forEach(r ->
                flightsList.add(
                        new Flight(
                                r.getInt("id"),
                                new Plane(r.getInt("plane_id")),
                                r.getString("start_city"),
                                r.getString("target_city")
                        ))
        );
        return flightsList;
    }

    public List<Hotel> selectAllHotels() {
        PreparedStatement SELECT_ALL_HOTELS = backendSession
                .session
                .prepare("SELECT * from hotels;");
        BoundStatement bs1 = new BoundStatement(SELECT_ALL_HOTELS);

        ResultSet rs1 = execute(bs1);
        List<Hotel> hotelsList = new ArrayList<>();
        rs1.forEach(r -> hotelsList.add(new Hotel(r.getInt("id"), r.getString("city"))));
        return hotelsList;
    }

    public List<Integer> selectAllFlightReservations(int flightId) {
        PreparedStatement SELECT_ALL_FLIGHT_RESERVATIONS = backendSession
                .session
                .prepare("select * from seat_reservations_by_customer_id where flight_id=?;");
        BoundStatement bs1 = new BoundStatement(SELECT_ALL_FLIGHT_RESERVATIONS);
        bs1.bind(flightId);

        ResultSet rs1 = execute(bs1);
        List<Integer> reservedSeats = new ArrayList<>();
        rs1.forEach(r -> reservedSeats.add(r.getInt("seat_id")));
        return reservedSeats;
    }


    public List<Integer> selectAllHotelReservations(int hotelId) {
        PreparedStatement SELECT_ALL_ROOM_RESERVATIONS = backendSession
                .session
                .prepare("select * from room_reservations_by_customer_id where hotel_id=?;");
        BoundStatement bs1 = new BoundStatement(SELECT_ALL_ROOM_RESERVATIONS);
        bs1.bind(hotelId);

        ResultSet rs1 = execute(bs1);
        List<Integer> reservedRooms = new ArrayList<>();
        rs1.forEach(r -> reservedRooms.add(r.getInt("room_id")));
        return reservedRooms;
    }

    public List<Hotel> selectAllHotelsInCity(String targetCity) {
        PreparedStatement SELECT_ALL_HOTELS = backendSession
                .session
                .prepare("SELECT * from hotels;");
        BoundStatement bs1 = new BoundStatement(SELECT_ALL_HOTELS);

        ResultSet rs1 = execute(bs1);
        List<Hotel> hotelsList = new ArrayList<>();
        rs1.forEach(r -> {
            if (r.getString("city").equals(targetCity)) {
                hotelsList.add(new Hotel(r.getInt("id"), r.getString("city")));
            }
        });
        return hotelsList;
    }

    public List<Integer> selectAllAvailableSeatsInFlight(int flightId) {
        PreparedStatement SELECT_ALL_AVAILABLE_SEATS = backendSession
                .session
                .prepare("SELECT * from available_plane_seats_by_flight WHERE flight_id=?;");
        BoundStatement bs1 = new BoundStatement(SELECT_ALL_AVAILABLE_SEATS);
        bs1.bind(flightId);
        ResultSet rs1 = execute(bs1);

        List<Integer> seatsIds = new ArrayList<>();
        rs1.forEach(r -> {
            if (r.getLong("available") == 1) {
                seatsIds.add(r.getInt("seat_id"));
            }
        });

        return seatsIds;
    }

    public void reserveSeatInFlight(Integer seatId, Flight flight, Integer customerId) {
        PreparedStatement INSERT_INTO_SEATS = backendSession
                .session
                .prepare("UPDATE available_plane_seats_by_flight SET available = available + 1 WHERE flight_id = ? and seat_id = ?;");

        BoundStatement bs1 = new BoundStatement(INSERT_INTO_SEATS);
        bs1.bind(flight.id, seatId);
        execute(bs1);

        PreparedStatement INSERT_INTO_SEATS_RESERVATION = backendSession
                .session
                .prepare("INSERT INTO seat_reservations_by_customer_id (seat_id, customer_id, flight_id, plane_id) VALUES (?,?,?,?);");
        BoundStatement bs2 = new BoundStatement(INSERT_INTO_SEATS_RESERVATION);
        bs2.bind(seatId, customerId, flight.id, flight.plane.id);
        execute(bs2);
    }

    public void reserveRoomInHotel(Room room, Hotel randomHotel, Integer customerId) {
        PreparedStatement INSERT_INTO_ROOMS = backendSession
                .session
                .prepare("UPDATE available_hotel_rooms_by_capacity SET available = available + 1 WHERE hotel_id = ? and capacity = ? and room_id = ?;");
        BoundStatement bs1 = new BoundStatement(INSERT_INTO_ROOMS);
        bs1.bind(randomHotel.id, room.capacity, room.id);
        execute(bs1);

        PreparedStatement INSERT_INTO_ROOM_RESERVATION = backendSession
                .session
                .prepare("INSERT INTO room_reservations_by_customer_id (hotel_id, room_id, customer_id) VALUES (?,?,?);");
        BoundStatement bs2 = new BoundStatement(INSERT_INTO_ROOM_RESERVATION);
        bs2.bind(randomHotel.id, room.id, customerId);
        execute(bs2);

    }

    public void cleanReservations() {
        PreparedStatement TRUNCATE_RESERVATION_SEATS = backendSession
                .session
                .prepare("TRUNCATE seat_reservations_by_customer_id;");
        BoundStatement bs1 = new BoundStatement(TRUNCATE_RESERVATION_SEATS);
        execute(bs1);

        PreparedStatement TRUNCATE_RESERVATION_ROOMS = backendSession
                .session
                .prepare("TRUNCATE room_reservations_by_customer_id;");
        BoundStatement bs2 = new BoundStatement(TRUNCATE_RESERVATION_ROOMS);
        execute(bs2);

        PreparedStatement TRUNCATE_HOTELS = backendSession
                .session
                .prepare("TRUNCATE hotels;");
        BoundStatement bs3 = new BoundStatement(TRUNCATE_HOTELS);
        execute(bs3);

        PreparedStatement TRUNCATE_FLIGHTS = backendSession
                .session
                .prepare("TRUNCATE flights;");
        BoundStatement bs4 = new BoundStatement(TRUNCATE_FLIGHTS);
        execute(bs4);

        PreparedStatement TRUNCATE_AVAIL_FLIGHTS = backendSession
                .session
                .prepare("TRUNCATE available_plane_seats_by_flight;");
        BoundStatement bs5 = new BoundStatement(TRUNCATE_AVAIL_FLIGHTS);
        execute(bs5);

        PreparedStatement TRUNCATE_AVAIL_ROOMS = backendSession
                .session
                .prepare("TRUNCATE available_hotel_rooms_by_capacity;");
        BoundStatement bs6 = new BoundStatement(TRUNCATE_AVAIL_ROOMS);
        execute(bs6);
    }

    public List<Room> selectAllAvailableRoomsInHotelWithCapacity(int hotelId, int capacity) {
        PreparedStatement SELECT_ROOMS = backendSession
                .session
                .prepare("SELECT * FROM srds.available_hotel_rooms_by_capacity where hotel_id=? and capacity >= ?;\n");
        BoundStatement bs1 = new BoundStatement(SELECT_ROOMS);
        bs1.bind(hotelId, capacity);
        ResultSet rs1 = execute(bs1);

        List<Room> rooms = new ArrayList<>();
        rs1.forEach(r -> {
            if (r.getLong("available") == 1) {
                rooms.add(new Room(r.getInt("room_id"), r.getInt("capacity")));
            }
        });

        return rooms;
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


}
