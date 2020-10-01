package bradesco.demo.order.flow;

import bradesco.demo.order.entity.Order;
import bradesco.demo.order.flow.model.OrderFlowContext;
import bradesco.demo.order.flow.model.RetrievePaymentCommandPayload;
import bradesco.demo.order.messaging.Producer;
import bradesco.demo.order.repository.OrderRepository;
import java.time.Duration;
import java.util.Collections;
import java.util.UUID;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;

@Component
public class RetrievePaymentAdapter implements JobHandler {

  @Autowired
  private Producer messageSender;

  @Autowired
  private OrderRepository orderRepository;

  @Autowired
  private ZeebeClient zeebe;

  private JobWorker subscription;

  @PostConstruct
  public void subscribe() {
    subscription = zeebe.newWorker()
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

    Order order = orderRepository.findById(Long.valueOf(context.getOrderId())).get();

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
