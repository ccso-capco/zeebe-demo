package bradesco.demo.payment;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableBinding(Source.class)
public class Producer {

//  private final KafkaTemplate<String, String> kafkaTemplate;

  private final MessageChannel output;
  private final ObjectMapper objectMapper;

  public void sendPaymentReceivedEvent(PaymentReceivedEventPayload payload, String correlationId) {
    Message<PaymentReceivedEventPayload> m = new Message<>("PaymentReceivedEvent", payload);
    m.setCorrelationid(correlationId);

    this.output.send(
        MessageBuilder.withPayload(asJson(m)).setHeader("type", m.getType()).build());

//    this.kafkaTemplate.send(
//        "payment-events",
//        asJson(MessageBuilder.withPayload(asJson(m)).setHeader("type", m.getType())));
  }

  private <T> String asJson(T message) {
    try {
      String json = objectMapper.writeValueAsString(message);
      return json;
    } catch (Exception e) {
      throw new RuntimeException("Could not transform and send message due to: "+ e.getMessage(), e);
    }
  }
}
