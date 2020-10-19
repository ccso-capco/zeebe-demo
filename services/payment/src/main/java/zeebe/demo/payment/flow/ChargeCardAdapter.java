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
import zeebe.demo.payment.messaging.model.RetrievePaymentCommandPayload;

@RequiredArgsConstructor
@Component
public class ChargeCardAdapter implements JobHandler {

  private static final String CARD_PAYMENT_URL = "http://payment-provider:8080/card-payments";

  private final RestTemplate restTemplate;
  private final ZeebeClient zeebeClient;
  private final ObjectMapper objectMapper;

  private JobWorker subscription;

  @PostConstruct
  public void subscribe() {
    subscription = zeebeClient.newWorker()
        .jobType("charge-creditcard")
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

    CreateCardPaymentRequest request = new CreateCardPaymentRequest();
    request.setAmount(message.getData().getAmount());
    CreateCardPaymentResponse response = restTemplate.postForObject(
        CARD_PAYMENT_URL,
        request,
        CreateCardPaymentResponse.class);

    client.newCompleteCommand(job.getKey())
        .variables(Collections.singletonMap("paymentTransactionId", response.getTransactionId()))
        .send().join();
  }
}
