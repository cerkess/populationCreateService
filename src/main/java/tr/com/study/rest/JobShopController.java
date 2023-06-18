package tr.com.study.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tr.com.study.JobShopRunnerBean;
import tr.com.study.api.createPopulation.CreatePopulationService;
import tr.com.study.data.DataHolder;
import tr.com.study.data.Task;

@RestController
@RequestMapping("/rest")

public class JobShopController {
     @Autowired
    JobShopRunnerBean jobShopRunnerBean;
     @Autowired
    CreatePopulationService createPopulationService;
     @Autowired
    DataHolder dataHolder;

    @GetMapping( "/start")
    ResponseEntity<String> returnRouteByVehiclePlate() {
        Task task1 = new Task(1,0,0,0, 3, 0, 0);
        Task task2 = new Task(2,0,0, 1, 2, 0, 1);
        Task task3 = new Task(3,0,0, 2, 2, 0, 2);
        Task task4 = new Task(4,0,0, 0, 2, 1, 0);
        Task task5 = new Task(5,0,0, 2, 1, 1, 1);
        Task task6 = new Task(6,0,0, 1, 4, 1, 2);
        Task task7 = new Task(7,0,0, 1, 4, 2, 0);
        Task task8 = new Task(8,0,0, 2, 3, 2, 1);
        dataHolder.insertConstraintMap(1,task1);
        dataHolder.insertConstraintMap(2,task2);
        dataHolder.insertConstraintMap(3,task3);
        dataHolder.insertConstraintMap(4,task4);
        dataHolder.insertConstraintMap(5,task5);
        dataHolder.insertConstraintMap(6,task6);
        dataHolder.insertConstraintMap(7,task7);
        dataHolder.insertConstraintMap(8,task8);

        jobShopRunnerBean.build(100);
        return new ResponseEntity<>("ok", HttpStatus.OK);
    }
}
