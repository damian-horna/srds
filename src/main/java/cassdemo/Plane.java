package cassdemo;

import cassdemo.backend.BackendSession;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Plane {
    public int id;
    public int rowNumber;
    public int rowSize;
    public BackendSession session;
    public HashMap<Integer, List<PlaneSeat>> seats = new HashMap<>();

    public Plane(BackendSession session, int id, int rowNumber, int rowSize) {
        this.session = session;
        this.id = id;
        this.rowNumber = rowNumber;
        this.rowSize = rowSize;

        for (int i=0; i < rowNumber; i++) {
            List<PlaneSeat> rowSeats = new ArrayList<>();
            for (int j=0; j<rowSize; j++){
                rowSeats.add(new PlaneSeat(i,j));
            }
            this.seats.put(i, rowSeats);
        }
    }
}
