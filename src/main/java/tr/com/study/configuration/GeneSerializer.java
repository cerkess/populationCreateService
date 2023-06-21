package tr.com.study.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.kafka.common.errors.SerializationException;
import org.apache.kafka.common.serialization.Serializer;
import tr.com.study.data.Gene;

import java.util.Map;

public class GeneSerializer implements Serializer<Gene> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        // No additional configuration needed
    }

    @Override
    public byte[] serialize(String topic, Gene data) {
        try {
            return objectMapper.writeValueAsBytes(data);
        } catch (Exception e) {
            throw new SerializationException("Error serializing object to JSON: " + e.getMessage(), e);
        }
    }

    @Override
    public void close() {
        // No resources to close
    }
}