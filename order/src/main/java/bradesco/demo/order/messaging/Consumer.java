package bradesco.demo.order.messaging;

import bradesco.demo.order.flow.model.PaymentReceivedEventPayload;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.client.ZeebeClient;
import java.util.Collections;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.StreamListener;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@EnableBinding(Sink.class)
public class Consumer {

  private final Producer producer;

  private final ObjectMapper objectMapper;

  private final ZeebeClient zeebe;

//  @KafkaListener(topics = "payment-events")
//  public void onPaymentRetrievedEvent() {
//    producer.sendOrderProcessedEvent(UUID.randomUUID().toString());
//  }

  @StreamListener(target = Sink.INPUT, condition = "(headers['type']?:'')=='PaymentReceivedEvent'")
  //@Transactional
  public void paymentReceived(String messageJson) throws Exception {
    Message<PaymentReceivedEventPayload> message = objectMapper.readValue(messageJson,
        new TypeReference<Message<PaymentReceivedEventPayload>>() {});

    PaymentReceivedEventPayload event = message.getData(); // TODO: Read something from it?

    zeebe.newPublishMessageCommand()
        .messageName(message.getType())
        .correlationKey(message.getCorrelationid())
        .variables(Collections.singletonMap("paymentInfo", "YeahWeCouldAddSomething"))
        .send().join();

    System.out.println("Correlated " + message);
  }
}
