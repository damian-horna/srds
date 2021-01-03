package cassdemo;

import cassdemo.domain.Flight;
import cassdemo.domain.Hotel;
import cassdemo.domain.Room;

import java.util.List;
import java.util.Random;

public class Customer implements Runnable {
    public int id;
    public int selectedUseCase;
    public DbService dbService;

    public Customer(int id, int selectedUseCase, DbService dbService) {
        this.id = id;
        this.selectedUseCase = selectedUseCase;
        this.dbService = dbService;
    }

    @Override
    public void run() {
        try {
            Thread.sleep((long) (Math.random() * 2000));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        switch (this.selectedUseCase) {
            case 1:
                useCase1();
                break;
            case 2:
                useCase2();
                break;
            case 3:
                useCase3();
                break;
        }
    }

    public void useCase1() {
        System.out.printf("Customer %d doing use case 1\n", this.id);
        Flight randomFlight = selectRandomFlight();
        List<List<Integer>> allPossibleSeats = findGroupOfSeatsInFlight(2, randomFlight);
        reserveRandomSeatsInFlight(randomFlight, allPossibleSeats, 1);
    }


    public void useCase2() {
        System.out.printf("Customer %d doing use case 2\n", this.id);
        Flight randomFlight = selectRandomFlight();
        List<List<Integer>> allPossibleSeats = findGroupOfSeatsInFlight(1, randomFlight);
        reserveRandomSeatsInFlight(randomFlight, allPossibleSeats, 2);
    }

    public void useCase3() {
        System.out.printf("Customer %d doing use case 3\n", this.id);
        Flight randomFlight = selectRandomFlight();
        List<List<Integer>> allPossibleSeats = findGroupOfSeatsInFlight(4, randomFlight);

        Hotel randomHotel = selectRandomHotelInCity(randomFlight.targetCity);
        if (randomHotel == null) {
            System.out.println("Customer " + this.id + " doing use case 3 reserve didn't find a hotel in city: " + randomFlight.targetCity);
        } else {
            List<Room> allAvailableRooms = this.dbService.selectAllAvailableRoomsInHotelWithCapacity(randomHotel.id, 4);
            if (allAvailableRooms.isEmpty()){
                System.out.println("Customer " + this.id + " doing use case 3 reserve didn't find a room in hotel");
            } else {
                reserveRandomSeatsInFlight(randomFlight, allPossibleSeats, 3);
                Room room = allAvailableRooms.get(new Random().nextInt(allAvailableRooms.size()));
                System.out.println("Customer " + this.id + " doing use case 3 reserve: " + room + "in: " + randomHotel);
                this.dbService.reserveRoomInHotel(room, randomHotel, this.id);
            }
        }
    }

    private Flight selectRandomFlight() {
        List<Flight> flights = this.dbService.selectAllFlights();
        return flights.get(new Random().nextInt(flights.size()));
    }

    private Hotel selectRandomHotelInCity(String targetCity) {
        List<Hotel> hotels = this.dbService.selectAllHotelsInCity(targetCity);
        if (hotels.isEmpty()) {
            return null;
        }
        return hotels.get(new Random().nextInt(hotels.size()));
    }

    private List<List<Integer>> findGroupOfSeatsInFlight(int groupSize, Flight flight) {
        List<Integer> availableSeats = this.dbService.selectAllAvailableSeatsInFlight(flight.id);
        return SeatsManager.findAllPossibleSeatsGroups(availableSeats, groupSize);
    }

    private void reserveRandomSeatsInFlight(Flight randomFlight, List<List<Integer>> allPossibleSeats, int use_case) {
        if (!allPossibleSeats.isEmpty()) {
            List<Integer> chosenSeats = allPossibleSeats.get(new Random().nextInt(allPossibleSeats.size()));
            for (Integer seat : chosenSeats) {
                this.dbService.reserveSeatInFlight(seat, randomFlight, this.id);
            }
            System.out.println("Customer " + this.id + " doing use case " + use_case + " reserve: " + chosenSeats + " in flight: " + randomFlight);
        } else {
            System.out.println("Customer " + this.id + " didn't find seats in flight: " + randomFlight);
        }
    }
}
