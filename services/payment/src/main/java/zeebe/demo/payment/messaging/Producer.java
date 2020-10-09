package zeebe.demo.payment.messaging;

import zeebe.demo.payment.messaging.model.Message;
import zeebe.demo.payment.messaging.model.PaymentReceivedEventPayload;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableBinding(Source.class)
public class Producer {

  private final MessageChannel output;
  private final ObjectMapper objectMapper;

  public void sendPaymentReceivedEvent(PaymentReceivedEventPayload payload, String correlationId) {
    Message<PaymentReceivedEventPayload> m = new Message<>("PaymentReceivedEvent", payload, correlationId);

    this.output.send(
        MessageBuilder.withPayload(asJson(m)).setHeader("type", m.getType()).build());
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
