package tr.com.study.configuration;

import com.fasterxml.jackson.databind.JsonSerializer;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.converter.StringJsonMessageConverter;
import tr.com.study.data.Gene;
import tr.com.study.data.Ugur;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaProducerConfig {

    @Bean
    public NewTopic topic() {
        return TopicBuilder.name("deneme")
                .partitions(4)
                .replicas(1)
                .build();
    }
    @Bean
    public ProducerFactory<String, Ugur> producerFactory() {
        Map<String, Object> configs = new HashMap<>();
        configs.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        configs.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        configs.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, UgurSerializer.class);

        return new DefaultKafkaProducerFactory<>(configs);
    }

//    @Bean
//    public KafkaTemplate<String, Gene> kafkaTemplate() {
//        KafkaTemplate<String, Gene> kafkaTemplate = new KafkaTemplate<>(producerFactory());
//        kafkaTemplate.setMessageConverter(new StringJsonMessageConverter());
//        return kafkaTemplate;
//    }

    @Bean
    public UgurSerializer ugurSerializer() {
        return new UgurSerializer();
    }
    @Bean
    public KafkaTemplate<String, Ugur> kafkaTemplate() {
        KafkaTemplate<String, Ugur> kafkaTemplate = new KafkaTemplate<>(producerFactory());
        return kafkaTemplate;
    }



}