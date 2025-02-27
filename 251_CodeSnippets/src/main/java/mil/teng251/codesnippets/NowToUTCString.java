package mil.teng251.codesnippets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * https://docs.oracle.com/javase/8/docs/api/java/time/Clock.html
 * The clock implementation provided here is based on System.currentTimeMillis().
 */
public class NowToUTCString implements SnipExec {
    private static final Logger logger = LoggerFactory.getLogger(NowToUTCString.class);

    private static String nowToUTCString() {
        OffsetDateTime dtm = OffsetDateTime.now(ZoneOffset.UTC);
        return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dtm);
    }

    public static void execWithNano() {
        OffsetDateTime dtm = OffsetDateTime.of(2017, 4, 11, 23, 55,
                10, 123456789, ZoneOffset.UTC);
        String output = DateTimeFormatter.ofPattern("YYYY-MM-dd'T'HH:mm:ss.SSS").format(dtm);
        logger.debug("execWithNano={}", output);//2017-04-11T23:55:10.123456789
    }

    @Override
    public void execute(String[] args) {
        logger.warn("TempNowToUTCStringExample - beg/2");
        String res = nowToUTCString();
        logger.warn("expected=!2025-02-14T06:30:09.196!");
        //result:         res=!2025-02-14T07:36:06.120!
        logger.warn("     res=!{}!", res);
        logger.warn("TempNowToUTCStringExample - end");
    }
}
