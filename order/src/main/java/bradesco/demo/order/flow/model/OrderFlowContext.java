package bradesco.demo.order.flow.model;

import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class OrderFlowContext {

  private String orderId;

  public static OrderFlowContext fromMap(Map<String, Object> values) {
    OrderFlowContext context = new OrderFlowContext();
    //context.traceId = (String) values.get("traceId");
    context.orderId = (String) values.get("orderId");
    return context;
  }

  public Map<String, String> asMap() {
    HashMap<String, String> map = new HashMap<>();
    map.put("orderId", orderId);
    return map;
  }
}
