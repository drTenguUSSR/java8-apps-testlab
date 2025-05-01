package mil.teng251.codesnippets;

import com.google.common.collect.ImmutableMap;
import mil.teng251.codesnippets.ntfs.NtfsStreamsInfo;
import org.apache.commons.cli.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            , "ntfs-streams-info", new NtfsStreamsInfo()
    );

    public static void main(String[] args) {


        logger.debug("app-beg");
        Options options = new Options();
        Option config = Option.builder("snip")
                .longOpt("snippetName")
                .numberOfArgs(1)
                .hasArg()
                .required(true)
                .desc("snippet:" + getSnips())
                .build();
        options.addOption(config);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            String snippetName = commandLine.getOptionValue("snippetName");
            logger.debug("snippetName={}", snippetName);
            if (!SNIPP_MAP.containsKey(snippetName)) {
                printHelp(options);
            }
            SnipExec handler = SNIPP_MAP.get(snippetName);
            handler.execute(args);
        } catch (ParseException ex) {
            printHelp(options);
            return;
        }
        logger.debug("app-end");
    }

    private static void printHelp(Options options) {
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java -jar Client.jar <option> <arguments>", "--- ---", options, "--- ---");
    }

    private static String getSnips() {
        List<String> m1 = SNIPP_MAP.entrySet().stream()
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        return String.join(", ", m1);
    }

}
