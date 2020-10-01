package bradesco.demo.payment;

import java.util.Date;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message<T> {

  private String type;
  private String id = UUID.randomUUID().toString();
  private String source = "Order Zeebe";
  private Date time = new Date();
  private T data;
  private String datacontenttype = "application/json";
  private String specversion = "1.0";

  // Extension attributes
  private String traceid = UUID.randomUUID().toString(); // trace id, default: new unique
  private String correlationid; // id which can be used for correlation later if required
  private String group = "order";

  public Message(String type, T payload) {
    this.type = type;
    this.data = payload;
  }
}
