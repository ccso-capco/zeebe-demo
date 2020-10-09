package zeebe.demo.shipment.messaging.model;

import java.util.Date;
import java.util.UUID;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Message<T> {

  private String type;
  private T data;

  private String id = UUID.randomUUID().toString();
  private String correlationId;
  private Date time = new Date();

  public Message(String type, T payload, String correlationId) {
    this.type = type;
    this.data = payload;
    this.correlationId = correlationId;
  }
}
