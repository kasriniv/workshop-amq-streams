//package com.redhat.devnation.kafka.streams.wordsrestservice;
package com.redhat.amqstreams.workshop.generic;
//writes whatever is POSTed to the /posttotopic endpoint to the my-topic topic
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.kafka.KafkaComponent;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class GenericProducerApplication {

	@Bean
	public RouteBuilder routeBuilder() {
		return new RouteBuilder() {
			@Override
			public void configure() throws Exception {

				routeBuilder()
						.restConfiguration("servlet")
						.bindingMode(RestBindingMode.auto);

				rest("/posttotopic")
						.post()
						.route()
						.log("${in.headers}")
						.inOnly("kafka:my-topic?brokers={{camel.kafka.bootstrap.servers}}")
						.endRest();

			}
		};
	}

	public static void main(String[] args) {
		SpringApplication.run(GenericProducerApplication.class, args);
	}
}
