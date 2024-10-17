package mil.teng.q2024.supply.warehouse.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import mil.teng.q2024.supply.warehouse.config.AppConfig;
import mil.teng.q2024.supply.warehouse.dto.EKeyboardResource;
import mil.teng.q2024.supply.warehouse.dto.SimpleDataResource;
import mil.teng.q2024.supply.warehouse.service.EKeyboardService;
import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Collections;
import java.util.Map;


@RestController
@RequestMapping("/main")
@Slf4j
@RequiredArgsConstructor
public class RootController {

    private final AppConfig appConfig;
    private final EKeyboardService eKeyboardService;
    private final RestTemplate restTemplate;

    @PostConstruct
    public void init() {
        log.debug("init-beg");
        log.debug("appConfig:{}", appConfig);
        log.debug("eKeyboardService:{}", eKeyboardService);
        log.debug("restTemplate:{}", restTemplate);
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

    @GetMapping({"/ping-tls/{key}", "/ping-tls/"})
    public ResponseEntity<SimpleDataResource> pingTls(@PathVariable(required = false) String key) {
        log.debug("pingTls beg. key={}", key);
        if (!StringUtils.hasText(key)) {
            StringBuilder sb = new StringBuilder();
            this.appConfig.getPingTls().forEach((ukey, uvalue) -> sb.append(ukey).append(","));
            SimpleDataResource result = new SimpleDataResource("error",
                    "key must not be empty. keys:" + sb.toString());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        String url = this.appConfig.getPingTls().get(key);
        if (!StringUtils.hasText(url)) {
            StringBuilder sb = new StringBuilder();
            this.appConfig.getPingTls().forEach((ukey, uvalue) -> sb.append(ukey).append(","));
            SimpleDataResource result = new SimpleDataResource("error",
                    "key [" + key + "] must be in tlsUrlMap. keys:" + sb.toString());
            return new ResponseEntity<>(result, HttpStatus.BAD_REQUEST);
        }
        log.debug("url={}", url);
        
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.set("User-Agent", "Application");
        HttpEntity<String> httpEntity = new HttpEntity<>(null, headers);

        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, httpEntity, String.class);

        log.debug("resp.code={}", resp.getStatusCode());
        log.debug("resp.text={}", resp.getBody());

        SimpleDataResource result = new SimpleDataResource("ping-tls: "
                + Instant.now().toString() + ", url=" + url,
                "code=" + resp.getStatusCode() + "\nbody=[\n" + resp.getBody() + "\n]");
        log.debug("pingTls end");
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

