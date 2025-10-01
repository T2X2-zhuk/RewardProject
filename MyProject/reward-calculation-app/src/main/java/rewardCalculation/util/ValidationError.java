package rewardCalculation.util;

import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ValidationError {

    private String errorCode;
    private String description;

}
