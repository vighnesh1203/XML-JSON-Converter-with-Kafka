package com.wissenkafka.flow;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.support.MessageBuilder;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;

@EnableKafka
@Configuration
public class KafkaIntegrationConfig {

	@Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
	
    @Bean
    public MessageChannel inputChannel() {
        return new DirectChannel();
    }

    @KafkaListener(topics = "xml-1-topic")
    public void listen(String xmlData) {
        System.out.println("Received XML: " + xmlData);
        inputChannel().send(MessageBuilder.withPayload(xmlData).build());
    }

    @Bean
    public IntegrationFlow kafkaListenerFlow() {
        return IntegrationFlow.from(inputChannel())
                .handle(handleMessage()) 
                .get();
    }

    @ServiceActivator(inputChannel = "inputChannel")
    public MessageHandler handleMessage() {
        return message -> {
            String xmlData = (String) message.getPayload();
            try {
                JSONObject json = XML.toJSONObject(xmlData);
                String jsonMessage = json.toString();
                System.out.println("Converted to JSON: " + jsonMessage);
                kafkaTemplate.send("json-1-topic", jsonMessage);
            } catch (Exception e) {
                System.err.println( e.getMessage());
            }
        };
    }
}
