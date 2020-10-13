package zeebe.demo.order;

import io.zeebe.client.ZeebeClient;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class Application {

  @Value("${zeebe.brokerContactPoint}")
  private String zeebeBrokerContactPoint;

  @Bean
  public ZeebeClient zeebe() {
    ZeebeClient zeebeClient = ZeebeClient.newClientBuilder()
        .brokerContactPoint(zeebeBrokerContactPoint)
        .usePlaintext()
        .build();

    // trigger deployment
    zeebeClient.newDeployCommand()
        .addResourceFromClasspath("order.bpmn")
        .send().join();

    return zeebeClient;
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}