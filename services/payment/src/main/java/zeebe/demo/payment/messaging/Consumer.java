package zeebe.demo.payment.messaging;

import io.zeebe.client.ZeebeClient;
import java.util.Collections;
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

  private final ZeebeClient zeebeClient;

  @StreamListener(target = Sink.INPUT,
      condition="(headers['type']?:'')=='RetrievePaymentCommand'")
  public void onRetrievePaymentCommand(String message) throws IOException {
    zeebeClient.newCreateInstanceCommand()
        .bpmnProcessId("payment-rest")
        .latestVersion()
        .variables(Collections.singletonMap("RetrievePaymentCommand", message))
        .send().join();
  }
}
