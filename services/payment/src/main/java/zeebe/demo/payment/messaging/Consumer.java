package zeebe.demo.payment.messaging;

import zeebe.demo.payment.messaging.model.Message;
import zeebe.demo.payment.messaging.model.PaymentReceivedEventPayload;
import zeebe.demo.payment.messaging.model.RetrievePaymentCommandPayload;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableBinding(Sink.class)
public class Consumer {

  private final Producer producer;

  private final ObjectMapper objectMapper;

  @StreamListener(target = Sink.INPUT,
      condition="(headers['type']?:'')=='RetrievePaymentCommand'")
  public void onRetrievePaymentCommand(String messageJson) throws IOException {
    Message<RetrievePaymentCommandPayload> message = objectMapper
        .readValue(messageJson, new TypeReference<Message<RetrievePaymentCommandPayload>>() {
        });
    RetrievePaymentCommandPayload retrievePaymentCommand = message.getData();

    PaymentReceivedEventPayload payload = PaymentReceivedEventPayload.builder()
        .orderId(retrievePaymentCommand.getOrderId())
        .build();

    producer.sendPaymentReceivedEvent(payload, message.getCorrelationId());
  }
}
