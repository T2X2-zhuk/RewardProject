package rewardCalculation.util.forErrors;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String errorCode;
    private String description;

}
