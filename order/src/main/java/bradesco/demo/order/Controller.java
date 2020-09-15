package bradesco.demo.order;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

  private final Producer producer;

  @GetMapping("/hello")
  public String hello() {
    return "greetings";
  }

  @PostMapping("/orders")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void createOrder() {
    producer.sendRetrievePaymentCommand();
  }
}