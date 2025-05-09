package mil.teng251.codesnippets;

import com.google.common.collect.ImmutableMap;
import lombok.extern.slf4j.Slf4j;
import mil.teng251.codesnippets.ntfs.NtfsStreamsInfo;
import org.apache.commons.cli.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * run via ide: run configuration/VM options:
 * -Djava.io.tmpdir=tmpFolder
 * -Dlog4j2.configurationFile=config/log4j2.xml
 * =============================================
 * актуально для NtfsStreamsInfo: компилировать под JDK 1.8-x64 для запуска на win-x64
 */
@Slf4j
public class App {
    private static final Map<String, SnipExec> SNIPP_MAP = ImmutableMap.of(
            "utc", new NowToUTCString()
            , "account-check", new CorrespondentAccountCheckDigit()
            , "n-streams", new NtfsStreamsInfo()
    );
    public static final String NTFS_VALIDATE_INTERNET_DOWNLOAD = "ntfs-validate-internet-download";
    public static final String NTFS_LOAD_ADS_LIMIT = "ntfs-load-ads-limit";

    public static void main(String[] args) throws IOException {
        log.debug("app-beg");

        Options options = new Options();
        Option config;
        config = Option.builder("snip")
                .longOpt("snippetName")
                .numberOfArgs(1)
                .hasArg()
                .required(true)
                .desc("snippet:" + getSnips())
                .build();
        options.addOption(config);

        config = Option.builder("p")
                .longOpt("path")
                .numberOfArgs(1)
                .hasArg()
                .required(false)
                .desc("(RESERVED! req: ntfs-streams-info) path for work dir")
                .build();
        options.addOption(config);

        config = Option.builder()
                .longOpt(NTFS_LOAD_ADS_LIMIT)
                .numberOfArgs(1)
                .hasArg()
                .required(false)
                .desc("(RESERVED! opt: n-streams) limit ADS size for load in Kb")
                .build();
        options.addOption(config);

        config = Option.builder()
                .longOpt(NTFS_VALIDATE_INTERNET_DOWNLOAD)
                .numberOfArgs(0)
                .required(false)
                .desc("(opt: n-streams) if present - validate 'Zone.Identifier' stream")
                .build();
        options.addOption(config);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine commandLine = parser.parse(options, args);
            String snippetName = commandLine.getOptionValue("snippetName");
            log.debug("snippetName={}", snippetName);
            if (!SNIPP_MAP.containsKey(snippetName)) {
                printHelp(options);
            }
            SnipExec handler = SNIPP_MAP.get(snippetName);
            if (handler==null) {
                printHelp(options);
                return;
            }
            handler.execute(commandLine);
        } catch (ParseException ex) {
            printHelp(options);
            return;
        }
        log.debug("app-end");
    }

    public static void printHelp(Options options) {
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
