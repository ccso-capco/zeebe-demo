package bradesco.demo.springboot1;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class Producer {

  private final KafkaTemplate<String, String> kafkaTemplate;

  public void sendCommand1() {
    this.kafkaTemplate.send("quickstart-commands", "command1");
  }

  public void sendEvent1() {
    this.kafkaTemplate.send("quickstart-events", "event1");
  }
}
