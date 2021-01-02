package cassdemo;

import cassdemo.backend.BackendSession;
import cassdemo.domain.Flight;
import cassdemo.domain.Hotel;

import java.util.List;

public class DbInitializer {
    DbService dbService;
    public DbInitializer(DbService dbService) {
        this.dbService = dbService;
    }

    public void initializeDb(List<Flight> flights, List<Hotel> hotels) {
        for (Flight f : flights){
            System.out.printf("Adding flight %d to DB%n", f.id);
            dbService.addFlight(f);
        }

        for (Hotel h : hotels){
            System.out.printf("Adding hotel %d to DB%n", h.id);
            dbService.addHotel(h);
        }
    }
}
