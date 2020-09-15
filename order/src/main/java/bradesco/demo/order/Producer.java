package bradesco.demo.order;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendRetrievePaymentCommand() {
    this.kafkaTemplate.send("payment-commands", "retrieve-payment");
  }

  public void sendOrderProcessedEvent() {
    this.kafkaTemplate.send("order-events", "order-processed");
  }
}
