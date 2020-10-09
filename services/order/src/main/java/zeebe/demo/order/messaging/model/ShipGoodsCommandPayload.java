package bradesco.demo.order.messaging.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipGoodsCommandPayload {

    private String orderId;
    private String logisticsProvider;
    private String recipientName;
    private String recipientAddress;
}
