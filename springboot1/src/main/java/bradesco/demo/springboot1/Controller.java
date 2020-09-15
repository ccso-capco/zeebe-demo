package bradesco.demo.springboot1;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class Controller {

  private final Producer producer;

  @GetMapping("/hello")
  public String hello() {
    return "greetings";
  }

  @PostMapping("/start")
  public String sendCommand() {
    producer.sendCommand1();
    return "accepted";
  }
}