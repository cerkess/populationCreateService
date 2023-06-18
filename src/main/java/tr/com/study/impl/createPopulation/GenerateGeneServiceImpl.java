package tr.com.study.impl.createPopulation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tr.com.study.api.createPopulation.GenerateGeneService;
import tr.com.study.data.DataHolder;
import tr.com.study.data.Gene;
import tr.com.study.data.Task;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class GenerateGeneServiceImpl implements GenerateGeneService {
    @Autowired
    DataHolder dataHolder;
    @Override
    public Gene generateGeneWithRandomMethod() {
        Map<Integer, Integer> machineTaskMap = dataHolder.getMachineTaskMap();
        int size = machineTaskMap.size();
        Set<Integer> idList = new HashSet<>(dataHolder.getConstraintMap().keySet());
        List<List<Task>> operationList = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<Task> row = new ArrayList<Task>();
            int count = machineTaskMap.get(i);
            for (int j = 0; j < count; j++) {
                assignRandomIdList(row,idList);
            }
            operationList.add(row);
        }
        Gene gene = new Gene(operationList,0);
        return gene;
    }

    @Override
    public Gene generateGeneWithMWRMethod() {
        return null;
    }

    @Override
    public Gene generateGeneWithMORMethod() {
        return null;
    }

    private void assignRandomIdList(List<Task> row ,Set<Integer> idList) {
        int id;
        id = ThreadLocalRandom.current().nextInt(1, 9);
        if (idList.size() == 0) {
            return;
        }
        if (idList.contains(id)) {
            Task task = dataHolder.getConstraintMapValue(id);
//            List<Integer> timeList=  stopTimeCalculator(task,12);
//            if(timeList.size()==0){
//                timeList = new ArrayList<>();
//                timeList.add(0);
//                timeList.add(0+task.getDuration());
//            }
//            Task result = new Task(task.getId(),timeList.get(0),timeList.get(1),task.getMachine(),task.getDuration(),task.getJobNum(),task.getPrecedenceJob());
            row.add(task);
            idList.remove(id);
        } else {
            assignRandomIdList(row,idList);
        }
    }

    @Override
    public Gene calculateStartStopTime(Gene gene, int timeLine) {
        List<List<Task>> taskList = new ArrayList<>();
        for(int i=0; i<gene.getGene().size(); i++){
            List<Task> tasks = new ArrayList<>();
            for(int j=0; j<gene.getGene().get(i).size(); j++){
                Task task = gene.getGene().get(i).get(j);
                tasks.add(stopTimeCalculator(task, 12));
            }
            taskList.add(tasks);
        }

        Gene copyGene = new Gene(taskList,0);
        Gene geneResult = new Gene(copyGene);
        return geneResult;
    }

    public Task  stopTimeCalculator(Task task, int timeLine) {
        int startTime = ThreadLocalRandom.current().nextInt(0, 13);
        int stopTime = startTime +task.getDuration();
        List<Integer> timeList = new ArrayList<>();
        if(stopTime>timeLine){
            return stopTimeCalculator(task,timeLine);
        }
        else {
            Task copy = new Task(task.getId(),startTime,stopTime,task.getMachine(),task.getDuration(),task.getJobNum(),task.getPrecedenceJob());
            return  copy;
        }
    }



}
