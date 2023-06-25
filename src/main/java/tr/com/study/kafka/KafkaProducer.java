package tr.com.study.kafka;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import tr.com.study.data.Gene;

import java.util.List;

@Service
public class KafkaProducer {
    @Autowired
    private StreamBridge streamBridge;

    public void sendMessage(Gene gene){
        streamBridge.send("producer-out-0",gene);
    }

}
