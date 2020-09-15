package bradesco.demo.springboot1;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Consumer {

  private final Producer producer;

  @KafkaListener(topics = "quickstart-events", groupId = "0")
  public void onEvent2() {
    producer.sendEvent1();
  }
}
