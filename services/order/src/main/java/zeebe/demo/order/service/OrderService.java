package bradesco.demo.order.service;

import bradesco.demo.order.entity.Order;
import bradesco.demo.order.repository.OrderRepository;
import java.util.Formatter;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderService {

  private final OrderRepository orderRepository;

  public Order create(Order order) {
    return orderRepository.save(order);
  }

  public Order requireById(long orderId) {
    return getById(orderId)
        .orElseThrow(() -> new RuntimeException(String.format("Order with id %d not found", orderId)));
  }

  private Optional<Order> getById(long orderId) {
    return orderRepository.findById(orderId);
  }
}
