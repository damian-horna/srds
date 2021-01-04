package cassdemo.domain;

public class PlaneSeat {
    public int id;
    public int rowNum;
    public int colNum;
    public int available = 0;

    public PlaneSeat(int id, int rowNum, int colNum) {
        this.id = id;
        this.rowNum = rowNum;
        this.colNum = colNum;
    }
}
