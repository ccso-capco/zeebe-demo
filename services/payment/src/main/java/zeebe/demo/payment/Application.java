package zeebe.demo.payment;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.worker.JobWorker;
import io.zeebe.model.bpmn.Bpmn;
import io.zeebe.model.bpmn.BpmnModelInstance;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
public class Application {

  @Value("${zeebe.brokerContactPoint}")
  private String zeebeBrokerContactPoint;

  @Bean
  public ZeebeClient zeebeClient() {
    ZeebeClient zeebeClient = ZeebeClient.newClientBuilder()
        .brokerContactPoint(zeebeBrokerContactPoint)
        .usePlaintext()
        .build();

    // trigger deployment
    zeebeClient.newDeployCommand()
        .addResourceFromClasspath("payment.bpmn")
        .send().join();

    return zeebeClient;
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }
}