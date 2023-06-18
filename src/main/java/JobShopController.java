import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;


public class JobShopController {

    Map<Integer, Task> constraintMap = new HashMap<>();
    List<Integer> idList = new ArrayList<>();

    void fillTheConstraintList() {
        Task task1 = new Task(1,0,0,0, 3, 0, 0);
        Task task2 = new Task(2,0,0, 1, 2, 0, 1);
        Task task3 = new Task(3,0,0, 2, 2, 0, 2);
        Task task4 = new Task(4,0,0, 0, 2, 1, 0);
        Task task5 = new Task(5,0,0, 2, 1, 1, 1);
        Task task6 = new Task(6,0,0, 1, 4, 1, 2);
        Task task7 = new Task(7,0,0, 1, 4, 2, 0);
        Task task8 = new Task(8,0,0, 2, 3, 2, 1);
        constraintMap.put(1, task1);
        idList.add(1);
        constraintMap.put(2, task2);
        idList.add(2);
        constraintMap.put(3, task3);
        idList.add(3);
        constraintMap.put(4, task4);
        idList.add(4);
        constraintMap.put(5, task5);
        idList.add(5);
        constraintMap.put(6, task6);
        idList.add(6);
        constraintMap.put(7, task7);
        idList.add(7);
        constraintMap.put(8, task8);
        idList.add(8);
    }

    List<List<Task>> generateGene() {
        Map<Integer, Integer> machineTaskMap = getMachineTaskMap();
        List<List<Task>> gene = new ArrayList<List<Task>>();
        int size = machineTaskMap.size();
        for (int i = 0; i < size; i++) {
            List<Task> row = new ArrayList<Task>();
            int count = machineTaskMap.get(i);
            for (int j = 0; j < count; j++) {
                assignRandomIdList(row);
            }
            gene.add(row);
        }
        return gene;
    }

    private void assignRandomIdList(List<Task> row) {
        int id;
        id = ThreadLocalRandom.current().nextInt(1, 9);
        if (idList.size() == 0) {
            return;
        }
        if (idList.contains(id)) {
            row.add(constraintMap.get(id));

            int index = idList.indexOf(id);
            idList.remove(index);
        } else {
            assignRandomIdList(row);
        }
    }

    private Map<Integer, List<List<Task>>> createPopulation(long size) {
        Map<Integer, List<List<Task>>> population = new HashMap<>();
        for (int i = 0; i < size; i++) {
            fillTheConstraintList();
            List<List<Task>> gene = generateGene();
            calculateStartStopTime(gene,12);
            population.put(i, gene);
        }
        return population;
    }

    private static List<List<Task>>  calculateStartStopTime(List<List<Task>> gene, int timeLine) {
        List<List<Task>> timedGene = new ArrayList<>();
        for (int i = 0; i < gene.size(); i++) {
            List<Task> timedGeneRow = new ArrayList<>();
            List<Task> row = gene.get(i);
            for (int j = 0; j < row.size(); j++) {
                Task task = stopTimeCalculator(j, row, timeLine);
                timedGeneRow.add(task);
            }
            timedGene.add(timedGeneRow);
        }
    return  timedGene;
    }

    private static Task stopTimeCalculator(int i, List<Task> row,int timeLine) {
        Task task = row.get(i);
        int startTime = ThreadLocalRandom.current().nextInt(0, 13);
        int stopTime = startTime +task.duration;
        if(stopTime>timeLine){
            return stopTimeCalculator(i,row,timeLine);
        }
        else {
            Task task1 = new Task(task.getId(),startTime,stopTime,task.getMachine(),task.getDuration(),task.jobNum,task.getPrecedenceJob());
            Task deepCopy = new Task(task1);
            return  deepCopy;
        }
    }


    private long calculateLCMOfPenaltyPoints(Set<Integer> penaltyPointSet, Map<Integer, List<List<Task>>> population) {
        population.forEach((key, gene) -> {
//            System.out.println("Gene " + key);
            int penaltyPoint = fitness(gene);
            penaltyPointSet.add(penaltyPoint);
        });

       return  Collections.max(penaltyPointSet)* Collections.min(penaltyPointSet);

//        return lcm_of_array_elements(penaltyPointSet.toArray());
    }

    private List<List<Task>> controlBestSolution(Map<Integer, List<List<Task>>> population) {
        for (Map.Entry<Integer, List<List<Task>>> entry : population.entrySet()) {
            Integer key = entry.getKey();
            List<List<Task>> gene = entry.getValue();
//            System.out.println("Gene " + key);
            int penaltyPoint = fitness(gene);
            if (penaltyPoint == 0) {
                System.out.println("Find best solution !!!!!!");
                return gene;
            }
        }
        return null;
    }

    private List<List<Integer>> crossOver(List<List<Task>> parentGene1, List<List<Task>> parentGene2) {
        int randomIndex = ThreadLocalRandom.current().nextInt(0, constraintMap.size());
        final int[] countparentGene1 = {0};
        final int[] countparentGene2 = {0};
        List<Integer> child1IdList = new ArrayList<>();
        List<Integer> child2IdList = new ArrayList<>();
        List<Integer> child2IdListAdded = new ArrayList<>();
        parentGene1.forEach(tasks -> tasks.forEach(task -> {
            if (countparentGene1[0] < randomIndex) {
                child1IdList.add(task.id);
            } else if (countparentGene1[0] >= randomIndex) {
                child2IdListAdded.add(task.id);
            }
            countparentGene1[0]++;
        }));
        parentGene2.forEach(tasks -> tasks.forEach(task -> {
            if (countparentGene2[0] >= randomIndex) {
                child1IdList.add(task.id);
            } else if (countparentGene2[0] < randomIndex) {
                child2IdList.add(task.id);
            }
            countparentGene2[0]++;
        }));
        child2IdList.addAll(child2IdListAdded);
        List<List<Integer>> returnedList = new ArrayList<>();
        returnedList.add(child1IdList);
        returnedList.add(child2IdList);
        return returnedList;
    }

    private List<Integer> mutation(List<Integer> childIdList) {
        HashSet<Integer> distinctChildIdSet = new HashSet<>(childIdList);
        List<Integer> willGiveIdList = new ArrayList<>(constraintMap.keySet());
        willGiveIdList.removeAll(distinctChildIdSet);
        Collections.shuffle(willGiveIdList);

        for (int i = 0; i < childIdList.size(); i++) {
            for (int j = 0; j < i; j++) {
                if (childIdList.get(i) == childIdList.get(j)) {
                    childIdList.set(i, willGiveIdList.get(willGiveIdList.size() - 1));
                    if (willGiveIdList.size() != 0) {
                        willGiveIdList.remove(willGiveIdList.size() - 1);
                    }
                }
            }
        }
        return childIdList;
    }

    private void printGene(List<List<Task>> gene) {
        final int[] machineNum = {0};
        gene.stream().forEach(tasks -> {

            System.out.println("Machine number " + machineNum[0]);
            tasks.forEach(task -> System.out.println(task.toString()));
            machineNum[0]++;
        });
    }

    private List<List<List<Task>>> generateWheel(Map<Integer, List<List<Task>>> population, long lcmOfArray) {
        List<List<List<Task>>> wheel = new ArrayList<>();
        population.forEach((key, gene) -> {
            int penaltyPoint = fitness(gene);

            long wheelTimes = lcmOfArray / penaltyPoint;
            for (int i = 0; i < wheelTimes; i++) {
                wheel.add(gene);
            }

        });
        return wheel;
    }

    private List<List<Task>> findAndReturnBestSolution(Map<Integer, List<List<Task>>> population) {
        AtomicReference<List<List<Task>>> resultGene = new AtomicReference<>();
        population.forEach((key, gene) -> {
            int penaltyPoint = fitness(gene);
            if (penaltyPoint == 0) {
                resultGene.set(gene);
                return;
            }
        });
        return resultGene.get();
    }

    private List<List<Task>> getGeneFromIdList(List<Integer> idList) {
        Map<Integer, Integer> machineTaskMap = getMachineTaskMap();
        List<List<Task>> gene = new ArrayList<>();
        int id = ThreadLocalRandom.current().nextInt(0, idList.size());
        for (int i = 0; i < machineTaskMap.size(); i++) {
            int count = machineTaskMap.get(i);
            List<Task> row = new ArrayList<>();
            for (int j = 0; j < count; j++) {
                Task task = constraintMap.get(idList.get(j));
                row.add(task);
            }
            for (int k = 0; k < count; k++) {
                idList.remove(0);
            }
            gene.add(row);
        }
        return gene;
    }



    Integer fitness(List<List<Task>> gene) {
        AtomicInteger machine = new AtomicInteger();
        AtomicInteger penaltyPoint = new AtomicInteger();
        machineAssigmentControl(gene, machine, penaltyPoint);
        precedenceControl(gene, penaltyPoint);
//        System.out.println("Penalty Point = " + penaltyPoint);
//        printGene(gene);
//        System.out.println("//////////////////////////////////");
        System.out.println(penaltyPoint.get());
        return penaltyPoint.get();
    }

    private static void precedenceControl(List<List<Task>> gene, AtomicInteger penaltyPoint) {
        Map<Integer, List<Task>> jobNumTaskMap = getIntegerListMap(gene);
            gene.forEach(taskList -> {
                        for (int i = 0; i < taskList.size(); i++) {
                            for (int j = 0; j < taskList.size(); j++) {
                                if (i != j) {
                                    if(taskList.get(i) != null && taskList.get(j) != null)
                                    controlTimeLine(taskList.get(i), taskList.get(j), penaltyPoint);
                                }
                            }
                        }
                    }
            );


        jobNumTaskMap.forEach((i, tasks) -> {
                for (int j = 0; j < tasks.size(); j++) {
                    if (i != j) {
                        if(i<tasks.size() && tasks.get(i) != null && tasks.get(j) != null)
                            controlTimePrecedence(tasks.get(i), tasks.get(j), penaltyPoint);
                    }
                }

        });
    }

    private static void controlTimePrecedence(Task task, Task task1,AtomicInteger penaltyPoint) {
        if(task.precedenceJob<task1.precedenceJob){
            if(!(task.stopTime<= task1.startTime)){
                penaltyPoint.getAndIncrement();
            }
        }else {
            if(!(task1.stopTime<= task.startTime)){
                penaltyPoint.getAndIncrement();
            }
        }
    }

    private static void controlTimeLine(Task task, Task task1,AtomicInteger penaltyPoint) {
        if((task.startTime+task.stopTime)/2<(task1.startTime+task1.stopTime)/2) {
            if (!(task.startTime <= task1.stopTime && task.startTime <= task1.startTime && task.stopTime <=task1.startTime &&task.stopTime<= task1.stopTime)){
                penaltyPoint.getAndIncrement();
            }
        }
        else{
            if(!(task.startTime >= task1.stopTime && task.startTime >= task1.startTime && task.stopTime >=task1.startTime &&task.stopTime>= task1.stopTime)){
                penaltyPoint.getAndIncrement();
            }

        }
    }

    private static Map<Integer, List<Task>> getIntegerListMap(List<List<Task>> gene) {
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

    private static void machineAssigmentControl(List<List<Task>> gene, AtomicInteger machine, AtomicInteger penaltyPoint) {
        gene.forEach(taskList -> {
            taskList.forEach(task -> {
                        if (task.machine != machine.get()) {
                            penaltyPoint.getAndIncrement();
                        }
                    }
            );
            machine.getAndIncrement();

        });
    }

    public static long lcm_of_array_elements(Object[] element_array) {
        long lcm_of_array_elements = 1;
        int divisor = 2;

        while (true) {
            int counter = 0;
            boolean divisible = false;

            for (int i = 0; i < element_array.length; i++) {

                // lcm_of_array_elements (n1, n2, ... 0) = 0.
                // For negative number we convert into
                // positive and calculate lcm_of_array_elements.

                if ((int) element_array[i] == 0) {
                    return 0;
                } else if ((int) element_array[i] < 0) {
                    element_array[i] = (int) element_array[i] * (-1);
                }
                if ((int) element_array[i] == 1) {
                    counter++;
                }

                // Divide element_array by devisor if complete
                // division i.e. without remainder then replace
                // number with quotient; used for find next factor
                if ((int) element_array[i] % divisor == 0) {
                    divisible = true;
                    element_array[i] = (int) element_array[i] / divisor;
                }
            }

            // If divisor able to completely divide any number
            // from array multiply with lcm_of_array_elements
            // and store into lcm_of_array_elements and continue
            // to same divisor for next factor finding.
            // else increment divisor
            if (divisible) {
                lcm_of_array_elements = lcm_of_array_elements * divisor;
            } else {
                divisor++;
            }

            // Check if all element_array is 1 indicate
            // we found all factors and terminate while loop.
            if (counter == element_array.length) {
                return lcm_of_array_elements;
            }
        }
    }


    Map<Integer, Integer> getMachineTaskMap() {
        Map<Integer, Integer> machineTaskMap = new HashMap<>();
        constraintMap.values().forEach(value -> {
            if (machineTaskMap.get(value.machine) == null) {
                machineTaskMap.put(value.machine, 0);
            }
            machineTaskMap.put(value.machine, machineTaskMap.get(value.machine) + 1);
        });
        return machineTaskMap;
    }


    public static void main(String args[]) {

        JobShopController jobShopController = new JobShopController();
//        List<List<Task>> gene = jobShopController.generateGene();
//        jobShopController.printGene(gene);
        int populationSize = 100;
        Map<Integer, List<List<Task>>> population = jobShopController.createPopulation(populationSize);
        List<List<Task>> result = jobShopGeneticAlgorithm(jobShopController, population, populationSize);
        jobShopController.printGene(result);


    }

    private static List<List<Task>> jobShopGeneticAlgorithm(JobShopController jobShopController, Map<Integer, List<List<Task>>> population, int populationSize) {
        int generation = 0;
        Set<Integer> penaltyPointSet = new HashSet<>();
        List<List<Task>> bestSolution = jobShopController.controlBestSolution(population);
        if (bestSolution != null) {
            return bestSolution;
        } else {
            long lcmOfArray = jobShopController.calculateLCMOfPenaltyPoints(penaltyPointSet, population);
            Map<Integer, List<List<Task>>> newPopulation = new HashMap<>();
            for (int i = 0; i < populationSize / 2; i++) {
//                GenerateWheelAndPickTwoGene generateWheelAndPickTwoGene = new GenerateWheelAndPickTwoGene(population, lcmOfArray, jobShopController).invoke();
                List<List<Task>> best = jobShopController.controlBestSolution(population);
//                List<List<Task>> gene1 = generateWheelAndPickTwoGene.getGene1();
//                List<List<Task>> gene2 = generateWheelAndPickTwoGene.getGene2();
                List<List<Task>> gene1 =   population.get(ThreadLocalRandom.current().nextInt(0, populationSize));
                List<List<Task>> gene2 =   population.get(ThreadLocalRandom.current().nextInt(0, populationSize));
//)
                List<List<Integer>> resultList = jobShopController.crossOver(gene1, gene2);
                List<Integer> result1IdList = jobShopController.mutation(resultList.get(0));
                List<Integer> result2IdList = jobShopController.mutation(resultList.get(1));
                List<List<Task>> resultGene1 = jobShopController.getGeneFromIdList(result1IdList);
                List<List<Task>> resultGene2 = jobShopController.getGeneFromIdList(result2IdList);
                if (newPopulation.get(newPopulation.size() - 1) == null) {
                    newPopulation.put(0, resultGene1);
                    newPopulation.put(1, resultGene2);
                } else {
                    int index = newPopulation.size();
                    newPopulation.put(index, resultGene1);
                    newPopulation.put(index + 1, resultGene2);
                }
            }
            System.out.println(generation);
            generation++;
            for(int i= 0; i<20;i++){
                int index = ThreadLocalRandom.current().nextInt(0, newPopulation.size());
                calculateStartStopTime(newPopulation.get(index),12);
            }
            return jobShopGeneticAlgorithm(jobShopController, newPopulation, populationSize);

        }
    }

    private static class GenerateWheelAndPickTwoGene {
        private Map<Integer, List<List<Task>>> population;
        private long lcmOfArray;
        private List<List<Task>> gene1;
        private List<List<Task>> gene2;
        private JobShopController jobShopController;

        public GenerateWheelAndPickTwoGene(Map<Integer, List<List<Task>>> population, long lcmOfArray, JobShopController jobShopController) {
            this.population = population;
            this.lcmOfArray = lcmOfArray;
            this.jobShopController = jobShopController;
        }

        public List<List<Task>> getGene1() {
            return gene1;
        }

        public List<List<Task>> getGene2() {
            return gene2;
        }

        public GenerateWheelAndPickTwoGene invoke() {
            List<List<List<Task>>> wheel = jobShopController.generateWheel(population, lcmOfArray);
//            for(int i=0; i<wheel.size()/10;i++) {
//                int index = ThreadLocalRandom.current().nextInt(0, wheel.size());
//               calculateStartStopTime(wheel.get(index),12);
//            }

            int wheelIndex1 = ThreadLocalRandom.current().nextInt(1, wheel.size());

            gene1 = wheel.get(wheelIndex1);
            int wheelIndex2 = ThreadLocalRandom.current().nextInt(1, wheel.size());
            gene2 = wheel.get(wheelIndex2);
            return this;
        }

    }
}

