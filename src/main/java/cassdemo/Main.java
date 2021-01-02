package cassdemo;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import cassdemo.backend.BackendException;
import cassdemo.backend.BackendSession;
import cassdemo.domain.Flight;
import cassdemo.domain.Hotel;
import cassdemo.domain.Plane;
import org.jetbrains.annotations.NotNull;

public class Main {

	private static final String PROPERTIES_FILENAME = "config.properties";
	private static final int PLANE_ID = 0;
	private static final int ROW_NUMBER = 12;
	private static final int ROW_SIZE = 6;
	private static final int NUM_OF_CUSTOMERS = 1000;

	public static void main(String[] args) throws IOException, BackendException {
		BackendSession session = createBackendSession();

		Plane plane = new Plane(session, PLANE_ID, ROW_NUMBER, ROW_SIZE);
		Hotel hotel = new Hotel(0, "Madrid", 10,10,10,10,10, 10);
		Flight flight = new Flight(0, plane, "Warsaw", "Madrid");

		DbService dbService = new DbService(session);

		List<Flight> flights = Arrays.asList(new Flight[]{flight});
		List<Hotel> hotels = Arrays.asList(new Hotel[]{hotel});

		initializeDb(flights, hotels, dbService);
		runCustomers(dbService);
		showStats();

		System.exit(0);
	}

	public static void initializeDb(List<Flight> flights, List<Hotel> hotels, DbService dbService){
		new DbInitializer(dbService).initializeDb(flights, hotels);
	}

	public static void runCustomers(DbService dbService){
		// Create NUM_OF_CUSTOMERS threads and start fighting for resources. Each customer:
		// 1. Looks up available seats (or groups of seats)
		// 2. Books one of the available seats (or groups of seats) and hotel if they want
		// At the end the app should collect statistics how many customers were successful with reservation and whether we faced some mistakes.
	}

	public static void showStats(){
		System.out.println("Stats: ...");
	}

	@NotNull
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
