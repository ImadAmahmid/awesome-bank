package com.awesome.bank.kafka.config;

import com.awesome.bank.event.BiEventSubscriber;
import com.awesome.bank.kafka.consumer.KafkaOperationBiEventConsumer;
import com.awesome.bank.kafka.generated.dto.OperationV2;
import com.awesome.bank.kafka.producer.AbstractKafkaBiEventProducer;
import com.awesome.bank.kafka.producer.KafkaOperationBiEventProducer;
import com.awesome.bank.kafka.service.NotificationSender;
//import io.confluent.kafka.serializers.KafkaAvroDeserializer;
//import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
//import io.confluent.kafka.serializers.KafkaAvroSerializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializer;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import io.confluent.kafka.serializers.KafkaAvroSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.NewTopic;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Configuration
@Profile("kafka")
public class KafkaConfig {

    @Autowired
    KafkaProperties properties;

    @Value("${event-producer.operation.topic.name}")
    private String operationsTopicName;

    @Bean
    public NewTopic createTopic() {
        return new NewTopic(operationsTopicName, 3, (short) 1);
    }

    @Bean
    public Map<String, Object> producerConfig() {
        LOG.info("[Kafka conf] creating producer config | bootstrap servers = [{}]", properties.getBootstrapServers());
        Map<String, Object> props = new HashMap<>();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, KafkaAvroSerializer.class);

        String schemaRegistery = properties.getProducer().getProperties().get("schema.registry.url");
        if (schemaRegistery != null) {
            props.put("schema.registry.url", schemaRegistery);
        }

        return props;
    }

    @Bean
    public ProducerFactory<String, OperationV2> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfig());
    }

    @Bean
    public ConsumerFactory<String, OperationV2> consumerFactory() {
        Map<String, Object> props = new HashMap<>();

        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, properties.getBootstrapServers());
        props.put(ConsumerConfig.GROUP_ID_CONFIG, properties.getConsumer().getGroupId());
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, KafkaAvroDeserializer.class);
        props.put(KafkaAvroDeserializerConfig.SPECIFIC_AVRO_READER_CONFIG, "true");
        String schemaRegistery = properties.getConsumer().getProperties().get("schema.registry.url");

        if (schemaRegistery != null) {
            props.put("schema.registry.url", schemaRegistery);
        }

        return new DefaultKafkaConsumerFactory<>(props);
    }

    @Bean
    public KafkaTemplate<String, OperationV2> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, OperationV2>
    filterKafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, OperationV2> factory =
                new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        return factory;
    }


//    My actual producer and consumer
    @Bean
    public BiEventSubscriber operationKafkaSubscriber() {
        return new KafkaOperationBiEventProducer();
    }

    @Bean
    public KafkaOperationBiEventConsumer operationBiEventConsumer(NotificationSender notificationSender) {
        return new KafkaOperationBiEventConsumer(notificationSender);
    }
}
