package bradesco.demo.order.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.Data;
import org.hibernate.annotations.Generated;

@Entity(name="OrderEntity")
@Data
public class Order {
  @Id @GeneratedValue(strategy= GenerationType.AUTO)
  //@GeneratedValue(generator = "uuid2")
  private long id;
  private int totalQuantity;
  private int totalSum;
}