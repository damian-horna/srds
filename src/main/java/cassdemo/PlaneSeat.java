package cassdemo;

public class PlaneSeat {
    public int id;
    public int rowNum;
    public int colNum;
    public boolean occupied = false;
    public int bookedBy = -1;

    public PlaneSeat(int id, int rowNum, int colNum) {
        this.id = id;
        this.rowNum = rowNum;
        this.colNum = colNum;
    }
}
