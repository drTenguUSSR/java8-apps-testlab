package mil.teng.q2024.supply.warehouse.dumper;

import org.slf4j.Logger;
import org.springframework.util.StreamUtils;
import org.springframework.web.util.ContentCachingRequestWrapper;

import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class RepeatableContentCachingRequestWrapper extends ContentCachingRequestWrapper {
    private final Logger log;
    private SimpleServletInputStream inputStream;

    public RepeatableContentCachingRequestWrapper(HttpServletRequest request, Logger log) {
        super(request);
        this.log=log;
    }

    @Override
    public ServletInputStream getInputStream() {
        return this.inputStream;
    }

    public String readInputAndDuplicate() throws IOException {
        if (inputStream == null) {
            byte[] body = StreamUtils.copyToByteArray(super.getInputStream());
            this.inputStream = new SimpleServletInputStream(body);
        }
        return new String(super.getContentAsByteArray(),StandardCharsets.UTF_8);
    }
}
