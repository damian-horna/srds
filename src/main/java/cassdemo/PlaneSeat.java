package cassdemo;

public class PlaneSeat {
    public int rowNum;
    public int colNum;
    public boolean occupied = false;
    public int bookedBy = -1;

    public PlaneSeat(int rowNum, int colNum) {
        this.rowNum = rowNum;
        this.colNum = colNum;
    }
}
