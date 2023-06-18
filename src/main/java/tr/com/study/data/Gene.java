package tr.com.study.data;

import java.io.Serializable;
import java.util.List;


public  class Gene implements Serializable {

    final List<List<Task>> gene;

    public List<List<Task>> getGene() {
        return gene;
    }

    public int getPenaltyPoint() {
        return penaltyPoint;
    }

    final int penaltyPoint;

    public Gene(List<List<Task>> gene, int penaltyPoint) {
        this.gene = gene;
        this.penaltyPoint = penaltyPoint;
    }

    public Gene(Gene that) {
        this(that.getGene(), that.getPenaltyPoint());
    }

    @Override
    public String toString() {
        return "Gene{" +
                "gene=" + gene +
                ", penaltyPoint=" + penaltyPoint +
                '}';
    }


}
