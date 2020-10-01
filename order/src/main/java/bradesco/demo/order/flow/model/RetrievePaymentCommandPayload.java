package bradesco.demo.order.flow.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RetrievePaymentCommandPayload {

    private String orderId;
    private int amount;
}
