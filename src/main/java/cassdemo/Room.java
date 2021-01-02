package cassdemo;

public class Room {
    public int id;
    public int capacity;
    public boolean occupied = false;

    public Room(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }
}
