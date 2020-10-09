package zeebe.demo.shipment.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import zeebe.demo.shipment.messaging.model.GoodsShippedEventPayload;
import zeebe.demo.shipment.messaging.model.Message;

@Component
@RequiredArgsConstructor
@EnableBinding(Source.class)
public class Producer {

  private final MessageChannel output;
  private final ObjectMapper objectMapper;

  public void sendGoodsShippedEvent(GoodsShippedEventPayload payload, String correlationId) {
    Message<GoodsShippedEventPayload> m = new Message<>("GoodsShippedEvent", payload, correlationId);

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
