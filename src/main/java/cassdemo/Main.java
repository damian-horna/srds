package cassdemo;

import cassdemo.backend.BackendException;
import cassdemo.backend.BackendSession;
import cassdemo.domain.Flight;
import cassdemo.domain.Hotel;
import cassdemo.domain.Plane;
import cassdemo.domain.Room;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static final String PROPERTIES_FILENAME = "config.properties";
    private static final int PLANE_ID = 0;
    private static final int ROW_NUMBER = 120;
    private static final int ROW_SIZE = 6;
    private static final int NUMBER_OF_ROOMS = 50;
    private static final int NUM_OF_CUSTOMERS = 1000;

    public static void main(String[] args) throws BackendException {
        Plane plane = new Plane(PLANE_ID, ROW_NUMBER, ROW_SIZE);
        Hotel hotel = new Hotel(0, "Madrid", NUMBER_OF_ROOMS, NUMBER_OF_ROOMS, NUMBER_OF_ROOMS, NUMBER_OF_ROOMS, NUMBER_OF_ROOMS, NUMBER_OF_ROOMS);
        Flight flight = new Flight(0, plane, "Warsaw", "Madrid");

        BackendSession session = createBackendSession();
        DbService dbService = new DbService(session);

        List<Flight> flights = Collections.singletonList(flight);
        List<Hotel> hotels = Collections.singletonList(hotel);

        initializeDb(flights, hotels, dbService);
        List<Customer> customers = runCustomers(dbService);
        showStats(dbService, customers);

        System.exit(0);
    }

    public static void initializeDb(List<Flight> flights, List<Hotel> hotels, DbService dbService) {
        new DbInitializer(dbService).initializeDb(flights, hotels);
    }

    public static List<Customer> runCustomers(DbService dbService) {
        List<Thread> threads = new ArrayList<>();
        List<Customer> customers = new ArrayList<>();

        for (int i = 0; i < NUM_OF_CUSTOMERS; i++) {
            int randIntUseCase = ThreadLocalRandom.current().nextInt(1, 4 + 1);
            Customer c = new Customer(i, randIntUseCase, dbService);
            customers.add(c);
        }

        for (int i = 0; i < NUM_OF_CUSTOMERS; i++) {
            Thread t = new Thread(customers.get(i));
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

        return customers;
    }

    public static void showStats(DbService dbService, List<Customer> customers) {
        System.out.println();
        System.out.println("STATS");
        List<Flight> flights = dbService.selectAllFlights();
        List<Hotel> hotels = dbService.selectAllHotels();
        long unique;
        System.out.println("CASSANDRA:");
        for (Flight flight : flights) {
            List<Integer> reservedSeats = dbService.selectAllFlightReservations(flight.id);
            unique = reservedSeats.stream().distinct().count();
            System.out.println("For " + flight + " we reserved " + reservedSeats.size() + "/" + ROW_NUMBER * ROW_SIZE + " seats. We have " + (reservedSeats.size() - unique) + " duplicated reservations.");
            List<Integer> availableSeats = dbService.selectAllAvailableSeatsInFlight(flight.id);
            System.out.println("We still have available " + availableSeats.size() + " seats, and for them we can reserve: ");
            System.out.println("2: [ " + SeatsManager.findAllPossibleSeatsGroups(availableSeats, 2).size() + " ], " +
                    "3: [ " + SeatsManager.findAllPossibleSeatsGroups(availableSeats, 3).size() + " ], " +
                    "4: [ " + SeatsManager.findAllPossibleSeatsGroups(availableSeats, 4).size() + " ], " +
                    "5: [ " + SeatsManager.findAllPossibleSeatsGroups(availableSeats, 5).size() + " ], " +
                    "6: [ " + SeatsManager.findAllPossibleSeatsGroups(availableSeats, 6).size() + " ], ");
            System.out.println("Remember that [1,2,3,4] create 3 possibilities how to reserve group of 2 seats.");
        }
        System.out.println();
        for (Hotel hotel : hotels) {
            List<Integer> reservedSeats = dbService.selectAllHotelReservations(hotel.id);
            unique = reservedSeats.stream().distinct().count();
            System.out.println("For " + hotel + " reserved " + reservedSeats.size() + "/" + NUMBER_OF_ROOMS * 6 + " rooms. " + (reservedSeats.size() - unique) + " of them were duplicated.");
            List<Room> allAvailableRooms = dbService.selectAllAvailableRoomsInHotelWithCapacity(hotel.id, 1);
            System.out.println("We still have available rooms with capacity: ");
            System.out.println("1: [  " + allAvailableRooms.stream().filter(o -> o.capacity == 1).count() + " ], " +
                    "2: [  " + allAvailableRooms.stream().filter(o -> o.capacity == 2).count() + " ], " +
                    "3: [  " + allAvailableRooms.stream().filter(o -> o.capacity == 3).count() + " ], " +
                    "4: [  " + allAvailableRooms.stream().filter(o -> o.capacity == 4).count() + " ], " +
                    "5: [  " + allAvailableRooms.stream().filter(o -> o.capacity == 5).count() + " ], " +
                    "6: [  " + allAvailableRooms.stream().filter(o -> o.capacity == 6).count() + " ], ");

        }
        System.out.println();
        System.out.println("CLIENT APP:");
        System.out.println("We had " + customers.size() + " customers and " + customers.stream().filter(o -> o.success).count() + " successfully reserved seats or rooms");
        System.out.println("We reserved " + customers.stream().filter(o -> o.success).mapToInt(o -> o.wantedSeats).sum() + " seats. " +
                "Successful seat request ratio: " + customers.stream().filter(o -> o.success).filter(o -> o.wantedSeats > 0).count() + "/" + customers.stream().filter(o -> o.wantedSeats > 0).count() + " requests.");
        System.out.println("We did not reserve " + customers.stream().filter(o -> !o.success).mapToInt(o -> o.wantedSeats).sum() + " seats, where quantities of groups sizes were:");
        System.out.println("1: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedSeats == 1).mapToInt(o -> o.wantedSeats).count() + " ], " +
                "2: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedSeats == 2).mapToInt(o -> o.wantedSeats).count() + " ], " +
                "3: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedSeats == 3).mapToInt(o -> o.wantedSeats).count() + " ], " +
                "4: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedSeats == 4).mapToInt(o -> o.wantedSeats).count() + " ], " +
                "5: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedSeats == 5).mapToInt(o -> o.wantedSeats).count() + " ], " +
                "6: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedSeats == 6).mapToInt(o -> o.wantedSeats).count() + " ], ");
        System.out.println("We reserved " + customers.stream().filter(o -> o.success).filter(o -> o.wantedRoomSize > 0).count() + " rooms." +
                "Successful room request ratio: " + customers.stream().filter(o -> o.success).filter(o -> o.wantedRoomSize > 0).count() + "/" + customers.stream().filter(o -> o.wantedRoomSize > 0).count() + " requests.");
        System.out.println("We did not reserve " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedRoomSize > 0).count() + " rooms, where expected sizes of rooms occur:");
        System.out.println("1: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedRoomSize == 1).mapToInt(o -> o.wantedRoomSize).count() + " ], " +
                "2: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedRoomSize == 2).mapToInt(o -> o.wantedRoomSize).count() + " ], " +
                "3: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedRoomSize == 3).mapToInt(o -> o.wantedRoomSize).count() + " ], " +
                "4: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedRoomSize == 4).mapToInt(o -> o.wantedRoomSize).count() + " ], " +
                "5: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedRoomSize == 5).mapToInt(o -> o.wantedRoomSize).count() + " ], " +
                "6: [  " + customers.stream().filter(o -> !o.success).filter(o -> o.wantedRoomSize == 6).mapToInt(o -> o.wantedRoomSize).count() + " ], ");

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
