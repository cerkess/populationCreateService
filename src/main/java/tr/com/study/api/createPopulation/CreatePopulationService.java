package tr.com.study.api.createPopulation;

import tr.com.study.data.Gene;
import tr.com.study.data.Task;

import java.util.List;

public interface CreatePopulationService {

    void fillConstraintList(int id, Task task);

    List<Gene> createPopulationWithMWR(int size);

    void createPopulationWithMOR(int size);

    List<Gene> createPopulationWithRandom(int size);



}
