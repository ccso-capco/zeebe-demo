package zeebe.demo.paymentprovider.controller;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import zeebe.demo.paymentprovider.controller.model.CreateCardPaymentRequest;
import zeebe.demo.paymentprovider.controller.model.CreateCardPaymentResponse;

@Slf4j
@RequiredArgsConstructor
@RestController
public class PaymentProviderController {

  @PostMapping("/card-payments")
  @ResponseStatus(HttpStatus.OK)
  public CreateCardPaymentResponse makeCardPayment(@RequestBody CreateCardPaymentRequest requestBody) {
    return CreateCardPaymentResponse.builder()
        .transactionId(UUID.randomUUID().toString())
        .build();
  }
}
