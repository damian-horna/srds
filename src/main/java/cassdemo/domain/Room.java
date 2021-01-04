package cassdemo.domain;

public class Room {
    public int id;
    public int capacity;
    public boolean available = true;

    public Room(int id, int capacity) {
        this.id = id;
        this.capacity = capacity;
    }

    @Override
    public String toString() {
        return "Room{" +
                "id=" + id +
                ", capacity=" + capacity +
                '}';
    }
}
