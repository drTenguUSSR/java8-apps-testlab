package mil.teng.q2024.supply.warehouse.controller;

import lombok.extern.slf4j.Slf4j;
import mil.teng.q2024.supply.warehouse.config.AppConfig;
import mil.teng.q2024.supply.warehouse.dto.EKeyboardResource;
import mil.teng.q2024.supply.warehouse.dto.SimpleDataResource;
import mil.teng.q2024.supply.warehouse.service.EKeyboardService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final EKeyboardService eKeyboardService;

    public RootController(AppConfig appConfig, EKeyboardService eKeyboardService) {
        log.debug(".ctor called");
        this.appConfig = appConfig;
        this.eKeyboardService = eKeyboardService;
    }

    @PostConstruct
    public void init() {
        log.debug("init-beg");
        log.debug("\tsimpleDebug={}", appConfig.isSimpleDebug());
        log.debug("\tdumpRequests={}", appConfig.isDumpRequests());
        log.debug("\tworkingData={}", appConfig.getWorkingData());
        log.debug("\tregistrationCallback={}", appConfig.getRegistrationCallback());
        log.debug("\ttaskA={}", appConfig.getTaskA());
        Map<String, String> tasks = appConfig.getScheduler();
        if (tasks != null) {
            tasks.forEach((key, value) -> {
                log.debug("\ttask: '{}' -> '{}'", key, value);
            });
        }
        log.debug("init-end");
    }

    @GetMapping(value = "/test-one")
    public ResponseEntity<SimpleDataResource> getTestOne() {
        SimpleDataResource result = new SimpleDataResource();
        result.setTextA("textA");
        result.setTextB("now=" + Instant.now().toString());
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping(value = "/test-two")
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

    @PostMapping(value = "/keyboard/create")
    public ResponseEntity<SimpleDataResource> keyboardCreate(@RequestBody EKeyboardResource requestData) {
        SimpleDataResource result = eKeyboardService.keyboardCreate(requestData);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping(value = "/keyboard/list-all")
    public ResponseEntity<SimpleDataResource> keyboardListAll(@RequestBody SimpleDataResource requestData) {
        return null;
    }
}

