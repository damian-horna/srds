package cassdemo;

import java.util.ArrayList;
import java.util.List;

public class Hotel {
    public int id;
    public List<Room> rooms = new ArrayList<>();

    public Hotel(int id, int numOfRooms1, int numOfRooms2, int numOfRooms3, int numOfRooms4, int numOfRooms5, int numOfRooms6) {
        this.id = id;

        addRoomsWithCapacity(numOfRooms1);
        addRoomsWithCapacity(numOfRooms2);
        addRoomsWithCapacity(numOfRooms3);
        addRoomsWithCapacity(numOfRooms4);
        addRoomsWithCapacity(numOfRooms5);
        addRoomsWithCapacity(numOfRooms6);
    }

    private void addRoomsWithCapacity(int capacity) {
        for (int i = 0; i < capacity; i++) {
            rooms.add(new Room(capacity));
        }
    }
}
