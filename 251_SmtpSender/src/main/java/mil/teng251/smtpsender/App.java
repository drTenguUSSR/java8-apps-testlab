package mil.teng251.smtpsender;

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
import java.util.Date;
import java.util.Properties;

/**
 * run via ide: run configuration/VM options:
 * -Dvar123=ide-vm-opt
 * -Djava.io.tmpdir=tmpFolder
 * -Dlog4j2.configurationFile=config/log4j2.xml
 * <p>
 * C:\Users\XXXX\.gradle\gradle.properties
 * gh251.smtp.senderA=mailA@mail.ru
 * gh251.smtp.senderB=mailB@mail.ru
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    public static boolean debug_env = false;
    // smtp-relay.gmail.com/smtp.gmail.com
    // use port 465 (for SSL), or port 587 (for TLS).
    public static int SmtpPort = 587;
    public static String SmtpPortStr = String.valueOf(SmtpPort);

    public static void main(String[] args) throws IOException {
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
        session.setDebug(true);

        String rcptA = prop.getProperty("rcpt.a");
        String rcptB = prop.getProperty("rcpt.b");
        logger.debug("-rcptA={}", rcptA);
        logger.debug("-rcptB={}", rcptB);

        String suffix = "04";
        String msgBody="проверка передачи данных"+suffix;
        sendEmail(session, prop.getProperty("mail.from"), prop.getProperty("mail.password"),
                rcptA, "subj-" + suffix, msgBody);

        logger.debug("App end");
    }

    private static Session getMailSession(Properties prop) {
        Properties mailProps = new Properties();

        mailProps.put("mail.smtp.starttls.enable", "true");
        mailProps.put("mail.smtp.starttls.required", "true");

        mailProps.put("mail.smtp.ssl.protocols", "TLSv1.2");
        mailProps.put("mail.smtp.ssl.checkserveridentity", true);
        mailProps.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        mailProps.put("mail.smtp.socketFactory.fallback", "false");

        mailProps.put("mail.smtp.auth", "true");
        mailProps.put("mail.smtp.EnableSSL.enable", "true");

        //mailProps.setProperty("mail.smtp.port", SmtpPortStr);
        //mailProps.setProperty("mail.smtp.socketFactory.port", SmtpPortStr);


        //create Authenticator object to pass in Session.getInstance argument
//        Authenticator auth = new Authenticator() {
//            //override the getPasswordAuthentication method
//            protected PasswordAuthentication getPasswordAuthentication() {
//                return new PasswordAuthentication(fromEmail, password);
//            }
//        };
        Session session = Session.getInstance(prop);
        return session;
    }

    //from:
    //  https://www.digitalocean.com/community/tutorials/javamail-example-send-mail-in-java-smtp
    //  https://stackoverflow.com/questions/15597616/sending-email-via-gmail-smtp-server-in-java

    //Sending e-mail with Java 21, SMTP & SSL in 2024
    //https://artofcode.wordpress.com/2023/12/31/sending-e-mail-with-java-21-smtp-ssl-in-2024/
    //smtp.jar-> https://javaee.github.io/javamail/
    private static void sendEmail(Session session, String authUser, String authPassword, String toEmail, String subject, String body) {
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

            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail, false));
            logger.debug("Message is ready. from={} to={} subj={} authUser={}", mailFrom, toEmail, subject, authUser);

            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.yandex.ru", authUser, authPassword);
            //transport.connect("smtp.gmail.com", SmtpPort, authUser, authPassword);
            logger.debug("transport-info: {}", transport);
            transport.sendMessage(msg, msg.getAllRecipients());

            //Transport.send(msg);

            logger.debug("EMail Sent Successfully!!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
