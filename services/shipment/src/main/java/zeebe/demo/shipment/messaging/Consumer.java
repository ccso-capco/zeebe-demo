package zeebe.demo.shipment.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import zeebe.demo.shipment.messaging.model.GoodsShippedEventPayload;
import zeebe.demo.shipment.messaging.model.Message;
import zeebe.demo.shipment.messaging.model.ShipGoodsCommandPayload;

@Component
@RequiredArgsConstructor
@EnableBinding(Sink.class)
public class Consumer {

  private final ObjectMapper objectMapper;

  private final Producer producer;

  @StreamListener(target = Sink.INPUT, condition="(headers['type']?:'')=='ShipGoodsCommand'")
  public void onShipGoodsCommand(String messageJson) throws IOException {
    Message<ShipGoodsCommandPayload> message = objectMapper
        .readValue(messageJson, new TypeReference<Message<ShipGoodsCommandPayload>>() {
        });
    ShipGoodsCommandPayload commandPayload = message.getData();

    GoodsShippedEventPayload eventPayload = GoodsShippedEventPayload.builder()
        .orderId(commandPayload.getOrderId())
        .build();

    producer.sendGoodsShippedEvent(eventPayload, message.getCorrelationId());
  }
}
