package tr.com.study.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import tr.com.study.data.Gene;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("erik")
                .partitions(1)
                .replicas(1)
                .build();
    }
    @Bean
    public ProducerFactory<String, Gene> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka-1:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, GeneSerializer.class);

        return new DefaultKafkaProducerFactory<>(configs);
    }

//    @Bean
//    public KafkaTemplate<String, Gene> kafkaTemplate() {
//        KafkaTemplate<String, Gene> kafkaTemplate = new KafkaTemplate<>(producerFactory());
//        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
//        return kafkaTemplate;
//    }

    @Bean
    public GeneSerializer geneSerializer() {
        return new GeneSerializer();
    }
    @Bean
    public KafkaTemplate<String, Gene> kafkaTemplate() {
        KafkaTemplate<String, Gene> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        return kafkaTemplate;
    }



}