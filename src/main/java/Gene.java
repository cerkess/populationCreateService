import java.util.List;

public class Gene {
    List<List<Task>> gene;
    int penaltyPoint;

    public List<List<Task>> getGene() {
        return gene;
    }

    public void setGene(List<List<Task>> gene) {
        this.gene = gene;
    }

    public int getPenaltyPoint() {
        return penaltyPoint;
    }

    public void setPenaltyPoint(int penaltyPoint) {
        this.penaltyPoint = penaltyPoint;
    }

}
