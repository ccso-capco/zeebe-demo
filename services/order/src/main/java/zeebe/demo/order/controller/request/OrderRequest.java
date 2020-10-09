package bradesco.demo.order.controller.request;

import lombok.Data;

@Data
public class OrderRequest {
  private int totalQuantity;
  private int totalSum;

  private String logisticsProvider;
  private String customerName;
  private String customerAddress;
}
