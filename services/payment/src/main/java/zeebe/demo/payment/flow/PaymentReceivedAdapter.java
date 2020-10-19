package zeebe.demo.payment.flow;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.ActivatedJob;
import io.zeebe.client.api.worker.JobClient;
import io.zeebe.client.api.worker.JobHandler;
import io.zeebe.client.api.worker.JobWorker;
import java.time.Duration;
import java.util.Collections;
import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import zeebe.demo.payment.client.CreateCardPaymentRequest;
import zeebe.demo.payment.client.CreateCardPaymentResponse;
import zeebe.demo.payment.messaging.Producer;
import zeebe.demo.payment.messaging.model.Message;
import zeebe.demo.payment.messaging.model.PaymentReceivedEventPayload;
import zeebe.demo.payment.messaging.model.RetrievePaymentCommandPayload;

@RequiredArgsConstructor
@Component
public class PaymentReceivedAdapter implements JobHandler {

  private final ZeebeClient zeebeClient;
  private final Producer producer;
  private final ObjectMapper objectMapper;

  private JobWorker subscription;

  @PostConstruct
  public void subscribe() {
    subscription = zeebeClient.newWorker()
        .jobType("payment-received")
        .handler(this)
        .timeout(Duration.ofMinutes(1))
        .open();
  }

  @PreDestroy
  public void closeSubscription() {
    subscription.close();
  }

  @Override
  public void handle(JobClient client, ActivatedJob job) throws JsonProcessingException {
    String messageJson = (String) job.getVariablesAsMap().get("RetrievePaymentCommand");

    Message<RetrievePaymentCommandPayload> message = objectMapper
        .readValue(messageJson, new TypeReference<Message<RetrievePaymentCommandPayload>>() {
        });

    client.newCompleteCommand(job.getKey())
        .send().join();

    PaymentReceivedEventPayload payload = PaymentReceivedEventPayload.builder()
        .orderId(message.getData().getOrderId())
        .build();

    producer.sendPaymentReceivedEvent(payload, message.getCorrelationId());
  }
}
