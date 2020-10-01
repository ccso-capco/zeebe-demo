package bradesco.demo.order.controller;

import bradesco.demo.order.controller.request.OrderRequest;
import bradesco.demo.order.entity.Order;
import bradesco.demo.order.flow.model.OrderFlowContext;
import bradesco.demo.order.messaging.Producer;
import bradesco.demo.order.repository.OrderRepository;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.ZeebeFuture;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class OrderController {

  private final Producer producer;
  private final OrderRepository repository;
  private final ModelMapper modelMapper;
  private final ZeebeClient zeebe;

  @PostMapping("/orders-zeebe")
  @ResponseStatus(HttpStatus.ACCEPTED)
  public void createOrderWithZeebe(@RequestBody OrderRequest requestBody) {
    Order order = modelMapper.map(requestBody, Order.class);
    repository.save(order);

    // prepare data for workflow
    OrderFlowContext context = new OrderFlowContext();
    context.setOrderId(String.valueOf(order.getId()));

    WorkflowInstanceEvent event = zeebe
        .newCreateInstanceCommand()
        .bpmnProcessId("order-kafka")
        .latestVersion()
        .variables(context.asMap())
        .send()
        .join();

    System.out.println(event.getWorkflowInstanceKey());
  }
}