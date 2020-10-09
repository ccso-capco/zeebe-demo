package bradesco.demo.order.flow;

import bradesco.demo.order.messaging.model.OrderCompletedEventPayload;
import bradesco.demo.order.messaging.Producer;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;
import java.time.Duration;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class OrderCompletedAdapter implements JobHandler {

  private final Producer producer;
  private final ZeebeClient zeebe;
  private JobWorker subscription;

  @PostConstruct
  public void subscribe() {
    subscription = zeebe.newWorker()
        .jobType("order-completed")
        .handler(this)
        .timeout(Duration.ofMinutes(1))
        .open();
  }

  @PreDestroy
  public void closeSubscription() {
    subscription.close();
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) {
    OrderFlowContext context = OrderFlowContext.fromMap(job.getVariablesAsMap());

    OrderCompletedEventPayload payload = OrderCompletedEventPayload.builder()
        .orderId(context.getOrderId())
        .build();

    producer.sendOrderCompletedEvent(payload);

    client.newCompleteCommand(job.getKey())
        .send().join();
  }
}
