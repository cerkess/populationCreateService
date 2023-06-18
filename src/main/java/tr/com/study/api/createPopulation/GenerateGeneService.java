package tr.com.study.api.createPopulation;

import org.springframework.stereotype.Service;
import tr.com.study.data.Gene;
import tr.com.study.data.Task;

import java.util.List;

public interface GenerateGeneService {
    Gene generateGeneWithRandomMethod();
    Gene generateGeneWithMWRMethod();
    Gene generateGeneWithMORMethod();

    Gene calculateStartStopTime(Gene gene ,int timeLine);

}
