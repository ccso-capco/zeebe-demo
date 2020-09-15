package bradesco.demo.order;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {

  private final Producer producer;

  @KafkaListener(topics = "payment-events")
  public void onPaymentRetrievedEvent() {
    producer.sendOrderProcessedEvent();
  }
}
