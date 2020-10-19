package zeebe.demo.order.flow;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;
import java.time.Duration;
import java.util.Collections;
import java.util.UUID;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import zeebe.demo.order.entity.Order;
import zeebe.demo.order.messaging.Producer;
import zeebe.demo.order.messaging.model.RetrievePaymentCommandPayload;
import zeebe.demo.order.service.OrderService;

@RequiredArgsConstructor
@Component
public class RetrievePaymentAdapter implements JobHandler {

  private final Producer messageSender;
  private final OrderService orderService;
  private final ZeebeClient zeebeClient;

  private JobWorker subscription;

  @PostConstruct
  public void subscribe() {
    subscription = zeebeClient.newWorker()
        .jobType("retrieve-payment")
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

    Order order = orderService.requireById(Long.parseLong(context.getOrderId()));

    RetrievePaymentCommandPayload payload = RetrievePaymentCommandPayload.builder()
        .orderId(String.valueOf(order.getId()))
        .amount(order.getTotalSum())
        .build();

    // generate an UUID for this communication
    String correlationId = UUID.randomUUID().toString();
    messageSender.sendRetrievePaymentCommand(payload, correlationId);

    client.newCompleteCommand(job.getKey())
        .variables(Collections.singletonMap("CorrelationId_RetrievePayment", correlationId))
        .send().join();
  }
}
