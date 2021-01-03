package cassdemo.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Plane {
    public int id;
    public int rowNumber;
    public int rowSize;
    public HashMap<Integer, List<PlaneSeat>> seats = new HashMap<>();

    public Plane(int id) {
        this.id = id;
    }

    public Plane(int id, int rowNumber, int rowSize) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.rowSize = rowSize;

        for (int i = 0; i < rowNumber; i++) {
            List<PlaneSeat> rowSeats = new ArrayList<>();
            for (int j = 0; j < rowSize; j++) {
                rowSeats.add(new PlaneSeat(i * rowSize + j, i, j));
            }
            this.seats.put(i, rowSeats);
        }
    }
}
