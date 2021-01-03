package cassdemo;

import cassdemo.domain.Flight;

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
        List<Flight> flights = this.dbService.selectAllFlights();
        Flight randomFlight = flights.get(new Random().nextInt(flights.size()));
        List<Integer> availableSeats = this.dbService.selectAllAvailableSeatsInFlight(randomFlight.id);
        List<List<Integer>> allPossibleSeats = SeatsManager.findAllPossibleSeatsGroups(availableSeats, 2);
        if (!allPossibleSeats.isEmpty()) {
            List<Integer> chosenSeats = allPossibleSeats.get(new Random().nextInt(allPossibleSeats.size()));
            for (Integer seat : chosenSeats) {
                this.dbService.reserveSeatInFlight(seat, randomFlight, this.id);
            }
            System.out.println("Customer " + this.id + " doing use case 1, reserve: " + chosenSeats + " in flight: " + randomFlight);
        } else {
            System.out.println("Customer " + this.id + " didn't find seats in flight: " + randomFlight);
        }
    }

    public void useCase2() {
        System.out.printf("Customer %d doing use case 2\n", this.id);
        List<Flight> flights = this.dbService.selectAllFlights();
        Flight randomFlight = flights.get(new Random().nextInt(flights.size()));
        List<Integer> availableSeats = this.dbService.selectAllAvailableSeatsInFlight(randomFlight.id);
        List<List<Integer>> allPossibleSeats = SeatsManager.findAllPossibleSeatsGroups(availableSeats, 1);
        if (!allPossibleSeats.isEmpty()) {
            List<Integer> chosenSeat = allPossibleSeats.get(new Random().nextInt(allPossibleSeats.size()));
            this.dbService.reserveSeatInFlight(chosenSeat.get(0), randomFlight, this.id);
            System.out.println("Customer " + this.id + " doing use case 2, reserve: " + chosenSeat + " in flight: " + randomFlight);
        } else {
            System.out.println("Customer " + this.id + " didn't find a seat in flight: " + randomFlight);
        }
    }

    public void useCase3() {
        System.out.printf("Customer %d doing use case 3\n", this.id);
    }

}
