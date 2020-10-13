package zeebe.demo.order.controller;

import io.zeebe.client.ZeebeClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import zeebe.demo.order.controller.request.OrderRequest;
import zeebe.demo.order.entity.Order;
import zeebe.demo.order.flow.OrderFlowContext;
import zeebe.demo.order.service.OrderService;

@Slf4j
@RequiredArgsConstructor
@RestController
public class OrderController {

  private final OrderService orderService;
  private final ModelMapper modelMapper;
  private final ZeebeClient zeebeClient;

  @PostMapping("/orders")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void createOrder(@RequestBody OrderRequest requestBody) {
    Order order = orderService.create(modelMapper.map(requestBody, Order.class));

    // prepare data for workflow
    OrderFlowContext context = new OrderFlowContext();
    context.setOrderId(String.valueOf(order.getId()));

    zeebeClient.newCreateInstanceCommand()
        .bpmnProcessId("order-kafka")
        .latestVersion()
        .variables(context.asMap())
        .send()
        .join();
  }
}