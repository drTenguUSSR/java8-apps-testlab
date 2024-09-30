package mil.teng.q2024.supply.warehouse;

import lombok.extern.slf4j.Slf4j;
import mil.teng.q2024.supply.warehouse.config.AppConfig;
import mil.teng.q2024.supply.warehouse.dto.SimpleDataResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Map;


@RestController
@RequestMapping("/main")
@Slf4j
public class RootController {

    private final AppConfig appConfig;

    public RootController(AppConfig appConfig) {
        log.debug(".ctor called");
        this.appConfig = appConfig;
    }

    @PostConstruct
    public void init() {
        log.debug("init-beg");
        log.debug("simpleDebug={}", appConfig.isSimpleDebug());
        log.debug("dumpRequests={}", appConfig.isDumpRequests());
        log.debug("workingData={}", appConfig.getWorkingData());
        log.debug("registrationCallback={}", appConfig.getRegistrationCallback());
        log.debug("taskA={}",appConfig.getTaskA());
        Map<String, String> tasks = appConfig.getScheduler();
        if (tasks != null) {
            tasks.forEach((key, value) -> {
                log.debug("task: '{}' -> '{}'", key, value);
            });

        }
        log.debug("init-end");
    }

    @PostMapping(value = "/testTwo")
    public ResponseEntity<SimpleDataResource> postTestTwo(@RequestBody SimpleDataResource requestData
            , @RequestParam Map<String, String> allParams, HttpServletRequest req) {
        String contentType = req.getHeader("Content-Type");
        log.debug("postTestTwo: textA={} textB={} ct={}", requestData.getTextA(), requestData.getTextB(), contentType);
        if (allParams == null || allParams.isEmpty()) {
            log.debug("allParams is null or empty");
        } else {
            log.debug("got params({}) =[", allParams.size());
            allParams.forEach((key, value) -> log.debug("- [{}] -> [{}]", key, value));
            log.debug("]");
        }
        SimpleDataResource result = new SimpleDataResource();
        result.setTextA(requestData.getTextA() + ":" + requestData.getTextB());
        result.setTextB("now=" + Instant.now().toString());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}

