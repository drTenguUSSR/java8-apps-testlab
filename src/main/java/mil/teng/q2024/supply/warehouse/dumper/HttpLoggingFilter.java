package mil.teng.q2024.supply.warehouse.dumper;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import mil.teng.q2024.supply.warehouse.config.AppConfig;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class HttpLoggingFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;
    private final AppConfig appConfig;

    public HttpLoggingFilter(ObjectMapper objectMapper, AppConfig appConfig) {
        this.objectMapper = objectMapper;
        this.appConfig = appConfig;
    }

    private static Map<String, String> collectHeaders(RepeatableContentCachingRequestWrapper requestWrapper) {
        Enumeration<String> headerNames = requestWrapper.getHeaderNames();
        Map<String, String> result = new HashMap<>();
        if (headerNames != null) {
            while (headerNames.hasMoreElements()) {
                String header = headerNames.nextElement();
                Enumeration<String> values = requestWrapper.getHeaders(header);
                StringBuilder sbValue = new StringBuilder();
                if (values != null) {
                    while (values.hasMoreElements()) {
                        sbValue.append(values.nextElement()).append(",");
                    }
                }
                result.put(header, sbValue.toString());
            }
        }
        return result;
    }

    private static boolean readyToWrite(Path outData) throws IOException {
        if (Files.exists(outData)) {
            log.warn("readyToWrite: duplicate name found {}. trying to delete",outData.toAbsolutePath());
            Files.delete(outData);
            if (Files.exists(outData)) {
                log.warn("readyToWrite: delete failed. dump skipped");
                return false;
            }
        }
        return true;
    }

    private static String makeMark() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss.SSS");
        String markDT = formatter.format(localDateTime);

        long unique = UUID.randomUUID().getLeastSignificantBits() & 0xffffffffL;
        String markUnique = String.format("%08x", unique);
        return markDT + "-" + markUnique;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!appConfig.isDumpRequests() && !appConfig.isDumpResponses()) {
            filterChain.doFilter(request, response);
        } else {
            RepeatableContentCachingRequestWrapper requestWrapper = new RepeatableContentCachingRequestWrapper(request,log);
//            ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

            String prefix = makeMark();
            if (appConfig.isDumpRequests()) {
                logRequest(requestWrapper, prefix);
            }
            filterChain.doFilter(requestWrapper, response); //responseWrapper
//            if (appConfig.isDumpResponses()) {
//                logResponse(responseWrapper, prefix);
//            }
        }
    }

    private void logRequest(RepeatableContentCachingRequestWrapper requestWrapper, String prefix) throws IOException {
        String body = requestWrapper.readInputAndDuplicate();
        DumpRequest fullRequest = new DumpRequest();
        fullRequest.setHeaders(collectHeaders(requestWrapper));
        fullRequest.setBody(body);
        fullRequest.setUrl(requestWrapper.getRequestURL().toString());
        fullRequest.setQuery(requestWrapper.getQueryString());
        String info = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(fullRequest);

        Path outData = Paths.get(appConfig.getDumpData(), prefix + "-req.txt");
        if (!readyToWrite(outData)) return;
        byte[] buffer = info.getBytes(StandardCharsets.UTF_8);
        Files.write(outData,buffer);
        log.debug("request writed to {}. length={} bytes", outData.toAbsolutePath(),buffer.length);
    }

    private void logResponse(ContentCachingResponseWrapper responseWrapper, String prefix) throws IOException {
        log.info("Response {}", new String(responseWrapper.getContentAsByteArray()));
        responseWrapper.copyBodyToResponse();
    }
}
