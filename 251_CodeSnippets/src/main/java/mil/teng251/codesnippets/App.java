package mil.teng251.codesnippets;

import com.google.common.collect.ImmutableMap;
import lombok.var;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

/**
 * run via ide: run configuration/VM options:
 * -Djava.io.tmpdir=tmpFolder
 * -Dlog4j2.configurationFile=config/log4j2.xml
 */
public class App {
    private static final Logger logger = LoggerFactory.getLogger(App.class);
    private static final Map<String, SnipExec> SNIPP_MAP = ImmutableMap.of(
            "utc", new NowToUTCString()
            , "account-check", new CorrespondentAccountCheckDigit()
    );

    public static void main(String[] args) {


        logger.debug("app-beg");
        Options options = new Options();
        Option config = Option.builder("r").longOpt("run")
                .argName("run")
                .hasArg()
                .required(true)
                .desc("exec one snippet").build();
        options.addOption(config);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            String snipName = commandLine.getOptionValue("r");
            logger.debug("snipName={}", snipName);
            if (!SNIPP_MAP.containsKey(snipName)) {
                printHelp(options);
            }
            SnipExec handler = SNIPP_MAP.get(snipName);
            handler.execute(args);
        } catch (ParseException ex) {
            printHelp(options);
            return;
        }
        logger.debug("app-end");
    }

    private static void printHelp(Options options) {
        StringBuilder sb = new StringBuilder();
        sb.append("available snippets is:");
        for (var dat : SNIPP_MAP.entrySet()) {
            sb.append(dat.getKey()).append(",");
        }
        String sbStr = sb.toString();
        sbStr = sbStr.substring(0, sbStr.length() - 1) + "\r\n\r\n";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar Client.jar <option> <arguments>", sbStr, options, "--- ---");
    }

}
