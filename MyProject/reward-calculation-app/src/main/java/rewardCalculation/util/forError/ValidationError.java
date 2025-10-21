package rewardCalculation.util.forError;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ValidationError {

    private String errorCode;
    private String description;

}
