package mil.teng.q2024.supply.warehouse.config;

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
    private boolean dumpResponses;
    private String dumpData;
    private String registrationCallback;
    private String taskA;
    private Map<String,String> scheduler;
}
