package bradesco.demo.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendPaymentRetrievedEvent() {
    this.kafkaTemplate.send("payment-events", "payment-retrieved");
  }
}
