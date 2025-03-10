package mil.teng251.smtpsender;

import com.google.common.base.Strings;
import mil.teng251.smtpsender.temp.BankAccountNumberValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

/**
 * run via ide: run configuration/VM options:
 * -Dvar123=ide-vm-opt
 * -Djava.io.tmpdir=tmpFolder
 * -Dlog4j2.configurationFile=config/log4j2.xml
 * <p>
 * C:\Users\XXXX\.gradle\gradle.properties
 * mail.from=xxxx@yandex.ru
 * mail.password=zzzz
 * rcpt.1=bugara@mail.ru
 * rcpt.2=tratata@mail.ru
 * rcpt.3=mew@mail.ru
 * rcpt.4=gav@mail.ru
 * <p>
 * xxxx@yandex.ru - имя для аутентификации на SMTP сервере яндекса
 * zzzz - пароль приложения (регистрируется на в настройках почты яндекса)
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final DateTimeFormatter DATETIME_STAMP = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    public static boolean debug_env = false;

    //temp code
    public static void main(String[] args) throws IOException {
        BankAccountNumberValidator.main(args);
    }

    public static void main2(String[] args) throws IOException {
        logger.debug("App begin/008");
        logger.debug("system-temp: {}", System.getProperty("java.io.tmpdir"));

        Properties prop = new Properties();

        File cfgFileLocal = new File("gh251.properties");
        File cfgFileHome = new File(System.getProperty("user.home") + File.separator + ".gradle" + File.separator + "gh251.properties");
        if (cfgFileLocal.exists() && cfgFileLocal.isFile() && cfgFileLocal.canRead()) {
            logger.debug("loading config from local file");
            try (InputStream stm = Files.newInputStream(cfgFileLocal.toPath())) {
                prop.load(stm);
            }
        } else if (cfgFileHome.exists() && cfgFileHome.isFile() && cfgFileHome.canRead()) {
            logger.debug("loading config from {}", cfgFileHome.getAbsolutePath());
            try (InputStream stm = Files.newInputStream(cfgFileHome.toPath())) {
                prop.load(stm);
            }
        } else {
            throw new RuntimeException("config missing. check:"
                    + "\n\tlocal file \"gh251.properties\""
                    + "\n\tfile:" + cfgFileHome.getAbsolutePath()
            );
        }

        Session session = getMailSession(prop);

        List<String> rcptList = new ArrayList<>();
        for (int i1 = 1; i1 < 10; i1++) {
            String address = prop.getProperty("rcpt." + i1);
            if (Strings.isNullOrEmpty(address)) {
                break;
            }
            rcptList.add(address);
        }

        String stampNow = getNowMark();
        String msgSubj = "subj for test message created at " + stampNow;
        String msgBody = createLatinAMessage(stampNow);

        logger.debug("msg.subj='{}'", msgSubj);
        logger.debug("rcpt({})={}", rcptList.size(), rcptList);

        sendEmail(session, prop.getProperty("mail.from"), prop.getProperty("mail.password"), rcptList, msgSubj, msgBody);

        logger.debug("App end");
    }

    private static Session getMailSession(Properties prop) {
        //https://javaee.github.io/javamail/docs/api/com/sun/mail/smtp/package-summary.html
        Properties mailProps = new Properties();

        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.starttls.required", "true");

        //mailProps.put("mail.smtp.ssl.protocols", "TLSv1.2 TLSv1.3");
        //mailProps.put("mail.smtp.ssl.checkserveridentity", true);
        //mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        //mailProps.put("mail.smtp.socketFactory.fallback", "false");
        //mailProps.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(prop);
        session.setDebug(true);
        return session;
    }

    //Sending e-mail with Java 21, SMTP & SSL in 2024
    //https://artofcode.wordpress.com/2023/12/31/sending-e-mail-with-java-21-smtp-ssl-in-2024/
    //smtp.jar-> https://javaee.github.io/javamail/
    private static void sendEmail(Session session, String authUser, String authPassword, List<String> toEmails, String subject, String body) {
        String mailFrom = "smith0477@yandex.ru";
        try {
            MimeMessage msg = new MimeMessage(session);
            msg.addHeader("Content-type", "text/HTML; charset=UTF-8");
            msg.addHeader("format", "flowed");
            msg.addHeader("Content-Transfer-Encoding", "8bit");

            msg.setFrom(new InternetAddress(mailFrom));
            msg.setReplyTo(InternetAddress.parse(mailFrom, true));
            msg.setSubject(subject, "UTF-8");
            msg.setText(body, "UTF-8");
            msg.setSentDate(new Date());

            if (toEmails.size() < 1) {
                throw new RuntimeException("toEmails.size()<1");
            }

            logger.debug("mail-to.set {}", toEmails.get(0));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmails.get(0), false));
            if (toEmails.size() > 1) {
                for (int i1 = 1; i1 < toEmails.size(); i1++) {
                    String address = toEmails.get(i1);
                    logger.debug("mail-to.add {}", address);
                    msg.addRecipients(Message.RecipientType.TO, address);
                }
            }

            logger.debug("Message is ready. from={} toEmails={} subj={} authUser={}", mailFrom, toEmails, subject, authUser);

            Transport transport = session.getTransport("smtps");
            //smtp.yandex.ru, 465 - worked (SSL)
            //smtp.yandex.ru, 587 - err (TLS. plain response, await StartTLS)
            transport.connect("smtp.yandex.ru", 465, authUser, authPassword);
            logger.debug("transport-info: {}", transport);
            transport.sendMessage(msg, msg.getAllRecipients());

            logger.debug("EMail Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String getNowMark() {
        LocalDateTime localDateTime = LocalDateTime.now();
        return DATETIME_STAMP.format(localDateTime);
    }

    private static String createLatinAMessage(String stampNow) {
        return "Hello, Mr. Test!\n"
                + "Now is " + stampNow + ".\n"
                + "Lorem ipsum odor amet, consectetuer adipiscing elit. Eros conubia mollis platea pharetra ornare" +
                " parturient ullamcorper! Aad conubia enim est egestas feugiat proin. Tempor nec magnis maecenas" +
                " convallis dapibus integer. Ultricies quis efficitur odio ipsum commodo per consectetur sodales." +
                " Ridiculus hendrerit integer est imperdiet vivamus mus. Purus taciti sed primis dictum" +
                " sollicitudin augue vulputate himenaeos.\n" +
                "\n" +
                "Lorem ad est porta nam convallis posuere mauris natoque. Facilisi in habitant ante neque lorem" +
                " orci. Mus libero habitant auctor mattis sapien maximus. Ex class litora nostra, ultricies massa" +
                " ligula. Massa viverra lorem cras erat nullam tempor nam dignissim. Nibh risus convallis hac montes" +
                " conubia. Egestas mi nisl dui dictum blandit. Efficitur pulvinar nostra posuere ipsum justo." +
                " Curae quisque erat lacinia suspendisse auctor fusce nec.\n" +
                "\n" +
                "Cubilia eros natoque quisque orci at magna sodales venenatis bibendum. Vel aliquet tempor" +
                " platea fringilla diam potenti ad. Dis mi feugiat at hac aliquet nisi per tellus orci? Sapien" +
                " sem facilisi maximus risus per a. Aliquam quisque eu; urna euismod odio turpis montes" +
                " sollicitudin. Eget vehicula ullamcorper sapien primis pharetra sed. Vestibulum mus magna" +
                " feugiat consectetur; auctor iaculis a. Suspendisse porttitor suscipit blandit cursus maximus ac" +
                " primis. Nec taciti mollis morbi non, tempor aptent augue.\n" +
                "---\n" +
                "With best regards Project 251";
    }
}
