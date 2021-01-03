package cassdemo;

import cassdemo.domain.Flight;

import java.util.ArrayList;
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
            Thread.sleep((long)(Math.random() * 2000));
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

        System.out.println(availableSeats);
        System.out.println(allPossibleSeats);

        List<Integer> chosenSeats = allPossibleSeats.get(new Random().nextInt(allPossibleSeats.size()));
        for (Integer seat : chosenSeats) {
           this.dbService.reserveSeatInFlight(seat, randomFlight, this.id);
        }
        System.out.println("Customer " + this.id + " doing use case 1, reserve: " + chosenSeats + " in flight: " + randomFlight);
    }

    public void useCase2() {
        System.out.printf("Customer %d doing use case 2\n", this.id);
    }

    public void useCase3() {
        System.out.printf("Customer %d doing use case 3\n", this.id);
    }

}
