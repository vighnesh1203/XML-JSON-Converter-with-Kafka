package com.wissenkafka;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.junit.jupiter.api.Test;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = { "xml-topic", "json-topic" })
class KafkaIntegrationApplicationTests {

    @Test
    void contextLoads() {
        // Your test logic here
    }
}
