package rewardPayment.lock;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "app")
@Data
public class AppLockProperties {

    private Map<String, String> locks = new HashMap<>();
}
