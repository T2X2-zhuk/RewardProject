package RewardCalculation.responses;
import RewardCalculation.util.CoreResponse;
import RewardCalculation.util.ValidationError;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RewardCalculationResponse extends CoreResponse {

    private String message;
    public RewardCalculationResponse(List<ValidationError> errors) {
        super(errors);
    }
}
