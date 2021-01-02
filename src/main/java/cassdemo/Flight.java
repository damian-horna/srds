package cassdemo;

public class Flight {
    public int id;
    public Plane plane;
    public String startCity;
    public String targetCity;

    public Flight(int id, Plane plane, String startCity, String targetCity) {
        this.id = id;
        this.plane = plane;
        this.startCity = startCity;
        this.targetCity = targetCity;
    }
}
