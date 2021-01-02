package cassdemo;

import cassdemo.domain.Flight;
import cassdemo.domain.Hotel;

import java.util.List;

public class DbInitializer {
    public void initializeDb(List<Flight> flights, List<Hotel> hotels) {
        for (Flight f : flights){
            System.out.printf("Adding flight %d to DB%n", f.id);
        }

        for (Hotel h : hotels){
            System.out.printf("Adding hotel %d to DB%n", h.id);
        }
    }
}
