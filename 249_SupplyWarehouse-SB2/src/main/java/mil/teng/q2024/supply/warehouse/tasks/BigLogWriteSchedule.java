package mil.teng.q2024.supply.warehouse.tasks;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.time.Instant;

/**
 * логирование - см. description\log2j2-config-2.17.2-rolling.xml
 */
@Component
@Slf4j
public class BigLogWriteSchedule {
    public static String taskName = "task-big-logger";
    private String bigString;

    @PostConstruct
    public void init() {
        StringBuilder sb = new StringBuilder();
        sb.append("beg!");
        for (int i1 = 0; i1 < 5; i1++) {
            sb.append("!").append(i1).append("!").append("1234567890z1234567890z1234567890z1234567890z1234567890z")
                    .append("1234567890z1234567890z1234567890z1234567890z1234567890z")
                    .append("1234567890z1234567890z1234567890z1234567890z1234567890z")
                    .append("1234567890z1234567890z1234567890z1234567890z1234567890z")
                    .append("1234567890z1234567890z1234567890z1234567890z1234567890z")
                    .append("qq,");
        }
        sb.append("end!");
        this.bigString = sb.toString();
        log.debug("init. sb.size={}", bigString.length());
    }

    @Scheduled(cron = "#{@appConfig.getScheduler().get(T(mil.teng.q2024.supply.warehouse.tasks.BigLogWriteSchedule).taskName)}")
    public void bigLog() throws InterruptedException {
        log.debug("====================================================================================");
        log.debug("now-A1={}\n{}", Instant.now(), bigString);
        Thread.sleep(100);
        log.debug("now-B1={}\n{}", Instant.now(), bigString);
        Thread.sleep(100);
        log.debug("now-C1={}\n{}", Instant.now(), bigString);
        Thread.sleep(100);
        log.debug("now-D1={}\n{}", Instant.now(), bigString);
        Thread.sleep(100);
        log.debug("now-A2={}\n{}", Instant.now(), bigString);
        Thread.sleep(100);
        log.debug("now-B2={}\n{}", Instant.now(), bigString);
        Thread.sleep(100);
        log.debug("now-C2={}\n{}", Instant.now(), bigString);
        Thread.sleep(100);
        log.debug("now-D2={}\n{}", Instant.now(), bigString);
        Thread.sleep(100);
    }
}
