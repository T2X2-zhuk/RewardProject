package rewardPayment.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import rewardPayment.dto.PaymentDTO;

import java.util.List;

@Data
@AllArgsConstructor
public class PaymentBatchMessage {

    private List<PaymentDTO> paymentDTOS;
}
