package mil.teng.q2024.supply.warehouse;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "supply.warehouse.config")
@Getter
@Setter
public class AppConfig {
    private boolean simpleDebug;
    private String workingData;
    private boolean dumpRequests;
    private String dumpData;
    private String registrationCallback;
    private Map<String,String> scheduler;

/**
 *     scheduler:
 *         lookup-new-certificate-response: 0/40 * * * * *
 */
}
