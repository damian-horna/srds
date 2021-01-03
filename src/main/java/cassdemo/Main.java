package cassdemo;

import cassdemo.backend.BackendException;
import cassdemo.backend.BackendSession;
import cassdemo.domain.Flight;
import cassdemo.domain.Hotel;
import cassdemo.domain.Plane;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static final String PROPERTIES_FILENAME = "config.properties";
    private static final int PLANE_ID = 0;
    private static final int ROW_NUMBER = 12;
    private static final int ROW_SIZE = 6;
    private static final int NUM_OF_CUSTOMERS = 70;

    public static void main(String[] args) throws BackendException {
        Plane plane = new Plane(PLANE_ID, ROW_NUMBER, ROW_SIZE);
        Hotel hotel = new Hotel(0, "Madrid", 5, 5, 5, 5, 5, 5);
        Flight flight = new Flight(0, plane, "Warsaw", "Madrid");

        BackendSession session = createBackendSession();
        DbService dbService = new DbService(session);

        List<Flight> flights = Collections.singletonList(flight);
        List<Hotel> hotels = Collections.singletonList(hotel);

        initializeDb(flights, hotels, dbService);
        runCustomers(dbService);
        showStats();

        System.exit(0);
    }

    public static void initializeDb(List<Flight> flights, List<Hotel> hotels, DbService dbService) {
        new DbInitializer(dbService).initializeDb(flights, hotels);
    }

    public static void runCustomers(DbService dbService) {
        List<Thread> threads = new ArrayList<>();
        for (int i = 0; i < NUM_OF_CUSTOMERS; i++) {
            int randIntUseCase = ThreadLocalRandom.current().nextInt(1, 4 + 1);
            Customer c = new Customer(i, randIntUseCase, dbService);
            Thread t = new Thread(c);
            t.start();
            threads.add(t);
        }

        for (Thread t : threads) {
            try {
                t.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void showStats() {
        System.out.println("Stats: ...");
    }

    private static BackendSession createBackendSession() throws BackendException {
        String contactPoint = null;
        String keyspace = null;

        Properties properties = new Properties();
        try {
            properties.load(Main.class.getClassLoader().getResourceAsStream(PROPERTIES_FILENAME));

            contactPoint = properties.getProperty("contact_point");
            keyspace = properties.getProperty("keyspace");
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new BackendSession(contactPoint, keyspace);
    }
}
