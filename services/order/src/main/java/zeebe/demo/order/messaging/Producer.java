package zeebe.demo.order.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import zeebe.demo.order.messaging.model.Message;
import zeebe.demo.order.messaging.model.OrderCompletedEventPayload;
import zeebe.demo.order.messaging.model.RetrievePaymentCommandPayload;
import zeebe.demo.order.messaging.model.ShipGoodsCommandPayload;

@Component
@RequiredArgsConstructor
@EnableBinding(Source.class)
public class Producer {

  private final MessageChannel output;
  private final ObjectMapper objectMapper;

  public void sendRetrievePaymentCommand(RetrievePaymentCommandPayload payload, String correlationId) {
    Message<RetrievePaymentCommandPayload> m = new Message<>("RetrievePaymentCommand", payload, correlationId);

    this.output.send(
        MessageBuilder.withPayload(asJson(m)).setHeader("type", m.getType()).build());
  }

  public void sendShipGoodsCommand(ShipGoodsCommandPayload payload, String correlationId) {
    Message<ShipGoodsCommandPayload> m = new Message<>("ShipGoodsCommand", payload, correlationId);
    m.setCorrelationId(correlationId);

    this.output.send(
        MessageBuilder.withPayload(asJson(m)).setHeader("type", m.getType()).build());
  }

  public void sendOrderCompletedEvent(OrderCompletedEventPayload payload) {
    Message<OrderCompletedEventPayload> m = new Message<>("OrderCompletedEvent", payload,
        UUID.randomUUID().toString());

    this.output.send(
        MessageBuilder.withPayload(asJson(m)).setHeader("type", m.getType()).build());
  }

  private <T> String asJson(T message) {
    try {
      String json = objectMapper.writeValueAsString(message);
      return json;
    } catch (Exception e) {
      throw new RuntimeException("Could not tranform and send message due to: "+ e.getMessage(), e);
    }
  }
}
