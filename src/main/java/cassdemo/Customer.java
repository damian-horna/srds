package cassdemo;

import cassdemo.domain.Flight;
import cassdemo.domain.Hotel;
import cassdemo.domain.Room;

import java.util.List;
import java.util.Random;

public class Customer implements Runnable {
    private int iterations;
    public int id;
    public int selectedUseCase;
    private final int delay;
    public DbService dbService;

    public Customer(int id, int selectedUseCase, DbService dbService) {
        this.id = id;
        this.selectedUseCase = selectedUseCase;
        this.dbService = dbService;
        this.delay = 100;
        this.iterations = 0;
    }

    @Override
    public void run() {
        while (this.iterations < 5) {
            try {
                Thread.sleep((long) (Math.random() * 100));
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
                case 4:
                    useCase4();
                    break;
            }
            this.iterations += 1;
        }

    }

    public void useCase1() {
        System.out.printf("Customer %d doing use case 1\n", this.id);
        Flight randomFlight = selectRandomFlight();
        List<List<Integer>> allPossibleSeats = findGroupOfSeatsInFlight(2, randomFlight);
        if (!allPossibleSeats.isEmpty()) {
            List<Integer> chosenSeats = allPossibleSeats.get(new Random().nextInt(allPossibleSeats.size()));
            for (Integer seat : chosenSeats) {
                this.dbService.reserveSeatInFlight(seat, randomFlight, this.id);
            }
            System.out.println("Customer " + this.id + " doing use case " + 1 + " reserve: " + chosenSeats + " in flight: " + randomFlight);
            try {
                Thread.sleep((long) (Math.random() * this.delay));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isFlightReservedOnlyByMe(chosenSeats, randomFlight, this.id)) {
                this.iterations = 10;
            } else {
                cancelSeatsReservation(chosenSeats, randomFlight);
            }
        } else {
            System.out.println("Customer " + this.id + " didn't find seats in flight: " + randomFlight);
        }
    }


    public void useCase2() {
        System.out.printf("Customer %d doing use case 2\n", this.id);
        Flight randomFlight = selectRandomFlight();
        List<List<Integer>> allPossibleSeats = findGroupOfSeatsInFlight(1, randomFlight);
        if (!allPossibleSeats.isEmpty()) {
            List<Integer> chosenSeats = allPossibleSeats.get(new Random().nextInt(allPossibleSeats.size()));
            for (Integer seat : chosenSeats) {
                this.dbService.reserveSeatInFlight(seat, randomFlight, this.id);
            }
            System.out.println("Customer " + this.id + " doing use case " + 2 + " reserve: " + chosenSeats + " in flight: " + randomFlight);
            try {
                Thread.sleep((long) (Math.random() * this.delay));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isFlightReservedOnlyByMe(chosenSeats, randomFlight, this.id)) {
                this.iterations = 10;
            } else {
                cancelSeatsReservation(chosenSeats, randomFlight);
            }
        } else {
            System.out.println("Customer " + this.id + " didn't find seats in flight: " + randomFlight);
        }
    }

    public void useCase3() {
        System.out.printf("Customer %d doing use case 3\n", this.id);
        Flight randomFlight = selectRandomFlight();
        List<List<Integer>> allPossibleSeats = findGroupOfSeatsInFlight(4, randomFlight);

        Hotel randomHotel = selectRandomHotelInCity(randomFlight.targetCity);
        if (randomHotel == null) {
            System.out.println("Customer " + this.id + " doing use case 3 didn't find a hotel in city: " + randomFlight.targetCity);
        } else {
            List<Room> allAvailableRooms = this.dbService.selectAllAvailableRoomsInHotelWithCapacity(randomHotel.id, 4);
            if (allAvailableRooms.isEmpty()) {
                System.out.println("Customer " + this.id + " doing use case 3 didn't find a room in hotel");
            } else {
                if (!allPossibleSeats.isEmpty()) {
                    List<Integer> chosenSeats = allPossibleSeats.get(new Random().nextInt(allPossibleSeats.size()));
                    for (Integer seat : chosenSeats) {
                        this.dbService.reserveSeatInFlight(seat, randomFlight, this.id);
                    }
                    System.out.println("Customer " + this.id + " doing use case " + 3 + " reserve: " + chosenSeats + " in flight: " + randomFlight);
                    Room room = allAvailableRooms.get(new Random().nextInt(allAvailableRooms.size()));
                    this.dbService.reserveRoomInHotel(room, randomHotel, this.id);
                    System.out.println("Customer " + this.id + " doing use case 3 reserve: " + room + " in: " + randomHotel);
                    if (isFlightReservedOnlyByMe(chosenSeats, randomFlight, this.id) && isHotelReservedOnlyByMe(randomHotel, room)) {
                        this.iterations = 10;
                    } else {
                        cancelSeatsReservation(chosenSeats, randomFlight);
                        this.dbService.removeRoomReservation(room, randomHotel, this.id);
                        System.out.println("Remove room reservation, customer: " + this.id + " room: " + room + " case: " + 3);
                    }
                } else {
                    System.out.println("Customer " + this.id + " didn't find seats in flight: " + randomFlight);
                }
            }
        }
    }


    public void useCase4() {
        List<Hotel> hotels = this.dbService.selectAllHotels();
        Hotel randomHotel = hotels.get(new Random().nextInt(hotels.size()));
        List<Room> allAvailableRooms = this.dbService.selectAllAvailableRoomsInHotelWithCapacity(randomHotel.id, 1);
        if (allAvailableRooms.isEmpty()) {
            System.out.println("Customer " + this.id + " doing use case 4 reserve didn't find a room in hotel");
        } else {
            Room room = allAvailableRooms.get(new Random().nextInt(allAvailableRooms.size()));
            this.dbService.reserveRoomInHotel(room, randomHotel, this.id);
            System.out.println("Customer " + this.id + " doing use case 4 reserve: " + room + " in: " + randomHotel);

            try {
                Thread.sleep((long) (Math.random() * this.delay));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (isHotelReservedOnlyByMe(randomHotel, room)) {
                this.iterations = 10;
            } else {
                System.out.println("Remove room reservation, customer: " + this.id + " room: " + room + " case: " + 4);
                this.dbService.removeRoomReservation(room, randomHotel, this.id);
            }
        }
    }


    private void cancelSeatsReservation(List<Integer> chosenSeats, Flight randomFlight) {
        System.out.println("Remove seat reservation : " + chosenSeats + " from flight: " + randomFlight + " customer: " + this.id);
        for (Integer seat : chosenSeats) {
            this.dbService.removeSeatReservationInFlight(seat, randomFlight, this.id);
        }
    }

    private boolean isHotelReservedOnlyByMe(Hotel randomHotel, Room room) {
        if (this.dbService.getRoomCounter(randomHotel, room) != 2) {
            return false;
        }
        List<Integer> reservations = this.dbService.selectAllHotelsReservationsReturnCustomerId(randomHotel, room);
        return reservations.size() == 1 && reservations.get(0) == this.id;
    }

    private boolean isFlightReservedOnlyByMe(List<Integer> chosenSeats, Flight randomFlight, int id) {
        for (Integer seat : chosenSeats) {
            if (this.dbService.getSeatCounter(seat, randomFlight) != 2) {
                return false;
            }
            List<Integer> reservations = this.dbService.selectAllSeatsReservationsWithSeatID(randomFlight, seat);
            if (!(reservations.size() == 1 && reservations.get(0) == this.id)) {
                return false;
            }
        }
        return true;
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

}
