package mil.teng.q2024.supply.warehouse.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleWrite2Console {
    public static String taskName="lookup-new-certificate-response";

    @Scheduled(cron = "#{@appConfig.getScheduler().get(T(mil.teng.q2024.supply.warehouse.tasks.SimpleWrite2Console).taskName)}")
    public void simple() {
        log.debug("simple runed");
    }
}
