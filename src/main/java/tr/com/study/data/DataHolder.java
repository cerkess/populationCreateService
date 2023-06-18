package tr.com.study.data;

import lombok.Getter;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Getter
public class DataHolder {
    Map<Integer, Task> constraintMap = new HashMap<>();

    public void insertConstraintMap(int id,Task task){
        constraintMap.put(id,task);
    }
    public Task getConstraintMapValue(int id){
        return  constraintMap.get(id);
    }

    public Map<Integer, Integer> getMachineTaskMap() {
        Map<Integer, Integer> machineTaskMap = new HashMap<>();
        constraintMap.values().forEach(value -> {
            if (machineTaskMap.get(value.getMachine()) == null) {
                machineTaskMap.put(value.getMachine(), 0);
            }
            machineTaskMap.put(value.getMachine(), machineTaskMap.get(value.getMachine()) + 1);
        });
        return machineTaskMap;
    }

    public  Map<Integer, List<Task>> getJobNumberTaskMap(List<List<Task>> gene) {
        Map<Integer, List<Task>> jobNumTaskMap = new HashMap<>();
        gene.forEach(taskList -> {
                    for (int i = 0; i < taskList.size(); i++) {
                        Task task =taskList.get(i);
                        if (jobNumTaskMap.get(task.jobNum) == null) {
                            List<Task> taskList1 = new ArrayList<>();
                            taskList1.add(task);
                            jobNumTaskMap.put(task.jobNum, taskList1);
                        } else {
                            List<Task> tasks = jobNumTaskMap.get(task.jobNum);
                            tasks.add(task);
                            jobNumTaskMap.put(task.jobNum, tasks);
                        }
                    }
                }
        );
        return jobNumTaskMap;
    }

    public  Gene updateGeneProcessTime(Gene gene,List<Task> taskList){
        List<Integer> idList = taskList.stream().map(task -> task.getId()).collect(Collectors.toList());
        Map<Integer,Task> listMap = taskList.stream().collect(Collectors.toMap(Task::getId, Function.identity()));
        List<List<Task>>  resultTaskList = new ArrayList<>();
        for(int i=0; i<gene.getGene().size(); i++){
            List<Task> tasks = new ArrayList<>();
            for(int j=0; j<gene.getGene().get(i).size(); j++){
                Task task = gene.getGene().get(i).get(j);
                if(idList.contains(task.getId())){
                    Task task1  = listMap.get(task.getId());
                    Task copyTask = new Task(task1.getId(),task1.getStartTime(),task1.getStopTime(),task1.getMachine(),task1.getDuration(),task1.getJobNum(),task1.getPrecedenceJob());
                    Task resultTask = new Task(copyTask);
                   tasks.add(resultTask);
                }else{
                    Task copyTask = new Task(task.getId(),task.getStartTime(),task.getStopTime(),task.getMachine(),task.getDuration(),task.getJobNum(),task.getPrecedenceJob());
                    Task resultTask = new Task(copyTask);
                    tasks.add(resultTask);
                }
            }
            resultTaskList.add(tasks);
        }
        Gene gene1 = new Gene(resultTaskList,0);
        Gene copyGene= new Gene(gene1);
        return  copyGene;
    }
}
