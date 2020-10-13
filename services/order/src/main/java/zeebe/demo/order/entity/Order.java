package zeebe.demo.order.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="OrderEntity")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {
  @Id @GeneratedValue(strategy= GenerationType.AUTO)
  private long id;

  private int totalQuantity;
  private int totalSum;

  private String logisticsProvider;
  private String customerName;
  private String customerAddress;
}