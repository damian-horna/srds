package cassdemo;

import java.io.IOException;
import java.util.Properties;

import cassdemo.backend.BackendException;
import cassdemo.backend.BackendSession;

public class Main {

	private static final String PROPERTIES_FILENAME = "config.properties";
	private static final int PLANE_ID = 0;
	private static final int ROW_NUMBER = 12;
	private static final int ROW_SIZE = 6;
	private static final int NUM_OF_CUSTOMERS = 1000;

	public static void main(String[] args) throws IOException, BackendException {
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
			
		BackendSession session = new BackendSession(contactPoint, keyspace);

		Plane plane = new Plane(session, PLANE_ID, ROW_NUMBER, ROW_SIZE);
		Hotel hotel = new Hotel(0, "Madrid", 10,10,10,10,10, 10);
		Flight flight = new Flight(0, plane, "Warsaw", "Madrid");


		// Create NUM_OF_CUSTOMERS threads and start fighting for resources. Each customer:
		// 1. Looks up available seats (or groups of seats)
		// 2. Books one of the available seats (or groups of seats) and hotel if they want
		// At the end the app should collect statistics how many customers were successful with reservation and whether we faced some mistakes.

		System.exit(0);
	}
}
