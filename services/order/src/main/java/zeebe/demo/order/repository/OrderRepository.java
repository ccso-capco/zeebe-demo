package zeebe.demo.order.repository;

import org.springframework.data.repository.CrudRepository;
import zeebe.demo.order.entity.Order;

public interface OrderRepository extends CrudRepository<Order, Long> {
}
