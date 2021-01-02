package cassdemo;

public class PlaneSeat {
    public int id;
    public int rowNum;
    public int colNum;
    public boolean available = true;

    public PlaneSeat(int id, int rowNum, int colNum) {
        this.id = id;
        this.rowNum = rowNum;
        this.colNum = colNum;
    }
}
