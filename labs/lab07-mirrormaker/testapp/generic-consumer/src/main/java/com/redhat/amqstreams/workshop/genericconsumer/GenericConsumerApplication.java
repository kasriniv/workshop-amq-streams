package com.redhat.amqstreams.workshop.genericconsumer;

import org.apache.camel.Exchange;
import org.apache.camel.Message;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaConstants;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.CopyOnWriteArrayList;

@SpringBootApplication
@RestController
public class GenericConsumerApplication {

	private ConcurrentMap<String,Long> wordCount = new ConcurrentHashMap<>();

	private List<WordCount> updates = new CopyOnWriteArrayList<>();

	@Bean
	public RouteBuilder routeBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {

				routeBuilder()
						.restConfiguration("servlet")
						.bindingMode(RestBindingMode.auto);

				from("kafka:my-topic?brokers={{camel.kafka.bootstrap.servers}}&valueDeserializer=org.apache.kafka.common.serialization.StringDeserializer&seekTo=beginning")
						.log("kavitha here"+"${in.headers[kafka.KEY]} : ${in.body}")
						.process(exchange -> {
                            Message in = exchange.getIn();
                            //wordCount.put(in.getHeader(KafkaConstants.KEY, String.class), in.getBody(Long.class));
                          
							// updates.add(new WordCount(in.getBody(String.class),in.getHeader(KafkaConstants.KEY, String.class)));
							 updates.add(
								 new WordCount(
									 in.getBody(String.class),"one more"));
							
                        });

				//rest("/word-count")
					//.get().route().setBody(constant(wordCount)).endRest();

				rest("/messages")
						.get().route().setBody(constant(updates)).endRest();
		

			}
			//int sizeOfList=updates.size();

			//System.out.println("Size of List :"+sizeOfList);
			
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(GenericConsumerApplication.class, args);
	}
}
