package tr.com.study;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.study.api.createPopulation.CreatePopulationService;
import tr.com.study.api.createPopulation.GenerateGeneService;
import tr.com.study.data.DataHolder;
import tr.com.study.data.Gene;
import tr.com.study.data.Task;

import javax.annotation.PostConstruct;
import java.util.List;

@Service
public class JobShopRunnerBean {

     static int generation = 0;

    @Autowired
    CreatePopulationService createPopulationService;

    @Autowired
    GenerateGeneService generateGeneService;

    @Autowired
    DataHolder dataHolder;
    
    @PostConstruct
    public void init() {

        Task task1 = new Task(1, 0, 0, 0, 3, 0, 0);
        Task task2 = new Task(2, 0, 0, 1, 2, 0, 1);
        Task task3 = new Task(3, 0, 0, 2, 2, 0, 2);
        Task task4 = new Task(4, 0, 0, 0, 2, 1, 0);
        Task task5 = new Task(5, 0, 0, 2, 1, 1, 1);
        Task task6 = new Task(6, 0, 0, 1, 4, 1, 2);
        Task task7 = new Task(7, 0, 0, 1, 4, 2, 0);
        Task task8 = new Task(8, 0, 0, 2, 3, 2, 1);
        dataHolder.  insertConstraintMap(1, task1);
        dataHolder.insertConstraintMap(2, task2);
        dataHolder.insertConstraintMap(3, task3);
        dataHolder.insertConstraintMap(4, task4);
        dataHolder.insertConstraintMap(5, task5);
        dataHolder.insertConstraintMap(6, task6);
        dataHolder.insertConstraintMap(7, task7);
        dataHolder.insertConstraintMap(8, task8);
        build(100);
    }


    public void build(Integer populationSize) {
        System.out.println("Start" + System.currentTimeMillis());
        List<Gene> population = createPopulationService.createPopulationWithMWR(populationSize);
        System.out.println("Population" + population);
    }


}
