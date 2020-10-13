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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import zeebe.demo.order.entity.Order;
import zeebe.demo.order.messaging.Producer;
import zeebe.demo.order.messaging.model.ShipGoodsCommandPayload;
import zeebe.demo.order.repository.OrderRepository;

@RequiredArgsConstructor
@Component
public class ShipGoodsAdapter implements JobHandler {

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
        .jobType("ship-goods")
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

    ShipGoodsCommandPayload payload = ShipGoodsCommandPayload.builder()
        .orderId(String.valueOf(order.getId()))
        .logisticsProvider(order.getLogisticsProvider())
        .recipientName(order.getCustomerName())
        .recipientAddress(order.getCustomerAddress())
        .build();

    // generate an UUID for this communication
    String correlationId = UUID.randomUUID().toString();
    messageSender.sendShipGoodsCommand(payload, correlationId);

    client.newCompleteCommand(job.getKey())
        .variables(Collections.singletonMap("CorrelationId_ShipGoods", correlationId))
        .send().join();
  }
}
