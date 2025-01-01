package mil.teng.q2024.supply.warehouse.consol;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class DateTimeFormatMark {
    public static void main(String[] args) {
        LocalDateTime localDateTime = LocalDateTime.now();

        xlog("mark-1=" + localDateTime);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH-mm-ss.SSS");
        String mark2 = formatter.format(localDateTime);
        xlog("mark-2=" + mark2);

        long uniq = UUID.randomUUID().getLeastSignificantBits() & 0xffffffffL;
        xlog("uniq-1=" + String.format("%025x", uniq));
    }

    public static void xlog(String msg) {
        System.out.println(msg);
    }
}
