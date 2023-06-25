package tr.com.study.impl.createPopulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.study.api.createPopulation.CreatePopulationService;
import tr.com.study.api.createPopulation.GenerateGeneService;
import tr.com.study.data.DataHolder;
import tr.com.study.data.Gene;
import tr.com.study.data.Task;
import tr.com.study.kafka.KafkaProducer;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class CreatePopulationServiceImpl  implements CreatePopulationService {

    @Autowired
    DataHolder dataHolder;

    @Autowired
    GenerateGeneService generateGeneService;

    @Autowired
    KafkaProducer kafkaProducer;


    @Override
    public void fillConstraintList(int id, Task task) {
           dataHolder.insertConstraintMap(id,task);
    }

    public CreatePopulationServiceImpl() {
        super();
    }

    @Override
    public List<Gene> createPopulationWithMWR(int size) {
      List<Gene> population= new ArrayList<>();
      
      for(int i=0;i<size;i++){
          List<Task> sortedDuration= new ArrayList<>(dataHolder.getConstraintMap().values()) ;
          sortedDuration.sort(Comparator.comparing(a -> a.getDuration()));
          Collections.reverse(sortedDuration);
          Integer[] idList = new Integer[dataHolder.getConstraintMap().size()];
          assignIdList(sortedDuration, idList);
          Gene copy= new Gene(getGeneFromIdList(Arrays.asList(idList)), 0);
          Gene timedGene = generateGeneService.calculateStartStopTime(copy,12);
          Gene gene = new Gene(timedGene);

          kafkaProducer.sendMessage(gene);

          population.add(gene);

      }
        return population;
        
    }

    private static void assignIdList(List<Task> sortedDuration, Integer[] idList) {
        List<Integer> indexList = new ArrayList<>();
        for(int k = 0; k< idList.length; k++){
            indexList.add(k);
        }
        for(int j = 0; j < sortedDuration.size(); j++) {
            Task task = sortedDuration.get(j);
            int index = ThreadLocalRandom.current().nextInt(0, indexList.size());
            int idValue = indexList.get(index);
            indexList.remove(index);
            if(idList[idValue] == null){
                idList[idValue] = task.getId();
            }
        }
    }

    public  List<List<Task>> getGeneFromIdList(List<Integer> idList) {
        List<Integer> returnedList = new ArrayList<>(idList);
        Map<Integer, Integer> machineTaskMap = dataHolder.getMachineTaskMap();
        List<List<Task>> gene = new ArrayList<>();
        for (int i = 0; i < machineTaskMap.size(); i++) {
            int count = machineTaskMap.get(i);
            List<Task> row = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                Task task = dataHolder.getConstraintMap().get(returnedList.get(j));
                row.add(task);
            }
            for (int k = 0; k < count; k++) {
                returnedList.remove(0);
            }
            gene.add(row);
        }
        return gene;
    }


    @Override
    public void createPopulationWithMOR(int size) {

    }

    @Override
    public  List<Gene> createPopulationWithRandom(int size) {
        List<Gene> population = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            Gene gene = generateGeneService.generateGeneWithRandomMethod();
            Gene timedGene = generateGeneService.calculateStartStopTime(gene,12);
            population.add(timedGene);
        }
        return population;
    }

}
