package mil.teng251.smtpsender.temp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;

/**
 * for IDE run: add VM options:
 * -Dlog4j2.configurationFile=config/log4j2.xml
 */
public class TempNowToUTCStringExample {
    private static final Logger logger = LoggerFactory.getLogger(TempNowToUTCStringExample.class);

    public static void main(String[] args) {
        logger.warn("TempNowToUTCStringExample - beg/2");
        String res = nowToUTCString();
        logger.warn("expected=!2025-02-14T06:30:09.196!");
        //result:         res=!2025-02-14T07:36:06.120!
        logger.warn("     res=!{}!", res);
        logger.warn("TempNowToUTCStringExample - end");
    }
    
    private static String nowToUTCString() {
        OffsetDateTime dtm = OffsetDateTime.now(ZoneOffset.UTC);
        String output = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(dtm);
        return output;
    }
}
