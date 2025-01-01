package mil.teng.q2024.supply.warehouse.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class SimpleScheduled {
    public static String taskName="task-simple";

    @Scheduled(cron = "#{@appConfig.getScheduler().get(T(mil.teng.q2024.supply.warehouse.tasks.SimpleScheduled).taskName)}")
    public void simple() {
        log.debug("simple runed");
    }
}
