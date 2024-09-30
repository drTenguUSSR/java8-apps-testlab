package mil.teng.q2024.supply.warehouse.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "supply.warehouse.config")
@Getter
@Setter
@Validated
public class AppConfig implements Validator {
    private boolean simpleDebug;
    private String workingData;
    private boolean dumpRequests;
    private boolean dumpResponses;
    private String dumpData;
    @NotBlank
    private String registrationCallback;
    @NotBlank
    private String taskA;
    private Map<String,String> scheduler;

    @Override
    public boolean supports(Class<?> clazz) {
        return AppConfig.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        AppConfig config= (AppConfig) target;

        String msg = directoryValidate(workingData, "workingData");
        if (msg!=null) {
            errors.reject("ERR",msg);
        }

    }

    private static String directoryValidate(String param,String field) {
        if (!StringUtils.hasText(param)) {
            return(field+". must not be empty!");
        }
        Path datPath = Paths.get(param);
        if (!Files.exists(datPath)) {
            return(field+".  path not exist!");
        }
        if(!Files.isDirectory(datPath)) {
            return(field+".  not a directory!");
        }
        return null;
    }


}
