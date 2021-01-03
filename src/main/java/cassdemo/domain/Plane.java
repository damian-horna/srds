package cassdemo.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

public class Plane {
    public int id;
    public Optional<Integer> rowNumber;
    public Optional<Integer> rowSize;
    public HashMap<Integer, List<PlaneSeat>> seats = new HashMap<>();

    public Plane(int id, Optional<Integer> rowNumber, Optional<Integer> rowSize) {
        this.id = id;
        this.rowNumber = rowNumber;
        this.rowSize = rowSize;

        if (rowNumber.isPresent() && rowSize.isPresent()) {
            for (int i = 0; i < rowNumber.get(); i++) {
                List<PlaneSeat> rowSeats = new ArrayList<>();
                for (int j = 0; j < rowSize.get(); j++) {
                    rowSeats.add(new PlaneSeat(i * rowSize.get() + j, i, j));
                }
                this.seats.put(i, rowSeats);
            }
        }

    }
}
