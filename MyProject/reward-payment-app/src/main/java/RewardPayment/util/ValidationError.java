package RewardPayment.util;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String errorCode;
    private String description;

}
