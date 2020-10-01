package bradesco.demo.order.messaging;

import bradesco.demo.order.flow.model.OrderCompletedEventPayload;
import bradesco.demo.order.flow.model.RetrievePaymentCommandPayload;
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

  public void sendRetrievePaymentCommand(RetrievePaymentCommandPayload payload, String correlationId) {
    Message<RetrievePaymentCommandPayload> m = new Message<>("RetrievePaymentCommand", payload);
    m.setCorrelationid(correlationId);

    this.output.send(
        MessageBuilder.withPayload(asJson(m)).setHeader("type", m.getType()).build());
  }

  public void sendOrderCompletedEvent(OrderCompletedEventPayload payload) {
    Message<OrderCompletedEventPayload> m = new Message<>("OrderCompletedEvent", payload);

    this.output.send(
        MessageBuilder.withPayload(asJson(m)).setHeader("type", m.getType()).build());
  }

//  public void sendRetrievePaymentCommand(String correlationId) {
//    Message<String> m = MessageBuilder.withPayload(correlationId)
//        .setHeader("type", "RetrievePaymentCommand")
//        .build();
//
//    try {
//      String jsonMessage = objectMapper.writeValueAsString(m);
//      this.kafkaTemplate.send("payment-commands", jsonMessage);
//    } catch (Exception e) {
//      throw new RuntimeException("Could not tranform and send message due to: "+ e.getMessage(), e);
//    }
//  }

//  public void sendOrderProcessedEvent(String correlationId) {
//    Message<String> m = MessageBuilder.withPayload(correlationId)
//        .setHeader("type", "OrderProcessedEvent")
//        .build();
//
//    try {
//      String jsonMessage = objectMapper.writeValueAsString(m);
//      this.kafkaTemplate.send("order-events", jsonMessage);
//    } catch (Exception e) {
//      throw new RuntimeException("Could not tranform and send message due to: "+ e.getMessage(), e);
//    }
//  }

  private <T> String asJson(T message) {
    try {
      String json = objectMapper.writeValueAsString(message);
      return json;
    } catch (Exception e) {
      throw new RuntimeException("Could not tranform and send message due to: "+ e.getMessage(), e);
    }
  }
}
