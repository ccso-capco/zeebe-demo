package bradesco.demo.payment;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {

  private final Producer producer;

  @KafkaListener(topics = "payment-commands")
  public void onRetrievePaymentCommand() {
    producer.sendPaymentRetrievedEvent();
  }
}
