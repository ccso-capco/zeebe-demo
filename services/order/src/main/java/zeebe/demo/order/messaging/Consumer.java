package zeebe.demo.order.messaging;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.client.ZeebeClient;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;
import zeebe.demo.order.messaging.model.GoodsShippedEventPayload;
import zeebe.demo.order.messaging.model.Message;
import zeebe.demo.order.messaging.model.PaymentReceivedEventPayload;

@Slf4j
@Component
@RequiredArgsConstructor
@EnableBinding(Sink.class)
public class Consumer {

  private final ObjectMapper objectMapper;

  private final ZeebeClient zeebe;

  @StreamListener(target = Sink.INPUT, condition = "(headers['type']?:'')=='PaymentReceivedEvent'")
  public void onPaymentReceivedEvent(String messageJson) throws Exception {
    Message<PaymentReceivedEventPayload> message = objectMapper.readValue(messageJson,
        new TypeReference<Message<PaymentReceivedEventPayload>>() {});

    zeebe.newPublishMessageCommand()
        .messageName(message.getType())
        .correlationKey(message.getCorrelationId())
        .variables(Collections.singletonMap("PaymentReceivedEvent", messageJson))
        .send().join();
  }

  @StreamListener(target = Sink.INPUT, condition = "(headers['type']?:'')=='GoodsShippedEvent'")
  public void onGoodsShippedEvent(String messageJson) throws Exception {
    Message<GoodsShippedEventPayload> message = objectMapper.readValue(messageJson,
        new TypeReference<Message<GoodsShippedEventPayload>>() {});

    zeebe.newPublishMessageCommand()
        .messageName(message.getType())
        .correlationKey(message.getCorrelationId())
        .variables(Collections.singletonMap("GoodsShippedEvent", messageJson))
        .send().join();
  }
}
