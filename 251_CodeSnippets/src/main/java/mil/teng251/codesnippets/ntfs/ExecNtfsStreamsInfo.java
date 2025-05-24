package mil.teng251.codesnippets.ntfs;

import com.google.common.base.Strings;
import de.vandermeer.asciitable.AT_Renderer;
import de.vandermeer.asciitable.AT_Row;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_FixedWidth;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;
import lombok.extern.slf4j.Slf4j;
import mil.teng251.codesnippets.SnipExec;
import mil.teng251.codesnippets.ntfs.wrapper.NtfsWrapper;
import org.apache.commons.cli.CommandLine;
import org.apache.tika.Tika;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * for IDE run:
 * VM options:
 * -Djava.io.tmpdir=tmpFolder -Dlog4j2.configurationFile=config/log4j2.xml
 * run argument:
 * run --args="-snippetName=n-streams -path=D:\INS\251-ntfs-multi"
 * =========================================================================
 * practical limit for streamName=255 chars
 * <p>
 * https://learn.microsoft.com/en-us/windows/win32/fileio/maximum-file-path-limitation?tabs=registry
 * The Windows API has many functions that also have Unicode versions to
 * permit an extended-length path for a maximum total
 * path length of 32,767 characters
 * To specify an extended-length path, use the "\\?\" prefix. For example, "\\?\D:\very long path".
 * <p>
 * https://colatkinson.site/windows/ntfs/2019/05/14/alternate-data-stream-size/
 * $ATTRIBUTE_LIST is capped at 256kb, which is exhausted more quickly due to the longer stream names.
 * (sum for all steam name length)
 * <p>
 * find stream 2 - https://learn.microsoft.com/en-us/windows/win32/api/fileapi/nf-fileapi-findfirststreamw
 */
@Slf4j
public class ExecNtfsStreamsInfo implements SnipExec {

    public static final String CMD_PATH = "path";
    public static final String VALIDATE_INTERNET_DOWNLOAD = "ntfs-validate-internet-download";
    public static final String LOAD_ADS_LIMIT = "ntfs-load-ads-limit";

    /**
     * https://stackoverflow.com/questions/3758606/how-can-i-convert-byte-size-into-a-human-readable-format-in-java
     *
     * @param bytes
     * @return
     */
    public static String humanReadableByteCountBin(long bytes) {
        long absB = bytes == Long.MIN_VALUE ? Long.MAX_VALUE : Math.abs(bytes);
        if (absB < 1024) {
            return bytes + " B";
        }
        long value = absB;
        CharacterIterator ci = new StringCharacterIterator("KMGTPE");
        for (int i = 40; i >= 0 && absB > 0xfffccccccccccccL >> i; i -= 10) {
            value >>= 10;
            ci.next();
        }
        value *= Long.signum(bytes);
        return String.format("%.1f %cB", value / 1024.0, ci.current());
    }

    @Override
    public void execute(CommandLine commandLine) throws IOException {
        String cmdPath = commandLine.getOptionValue(CMD_PATH);
        if (Strings.isNullOrEmpty(cmdPath)) {
            throw new IllegalArgumentException("param '-path' is null or empty!");
        }
        boolean cmdValidateInternet = commandLine.hasOption(VALIDATE_INTERNET_DOWNLOAD);
        int cmdAdsLimit = Integer.parseInt(commandLine.getOptionValue(LOAD_ADS_LIMIT, "0"));
        log.debug("cmdPath={} validateInternet={} cmdAdsLimit={}", cmdPath, cmdValidateInternet, cmdAdsLimit);

        Path paramPath = Paths.get(cmdPath);
        if (!Files.exists(paramPath)) {
            throw new IllegalArgumentException("path [" + cmdPath + "] not exist");
        }
        boolean isDirectory = Files.isDirectory(paramPath);
        log.debug("isDirectory={}", isDirectory);
        
        List<StreamInfo> streamsList = new ArrayList<>();
        if (isDirectory) {
            //test-fix-work-beg
            //processTestFixWork(cmdPath);
            //test-fix-work-end
            Set<String> allFiles;
            try (Stream<Path> streamItem = Files.list(paramPath)) {
                allFiles = streamItem
                        .filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .collect(Collectors.toSet());
            }
            for (String xfile : allFiles) {
                processFile(cmdPath, streamsList, xfile);
            }
        } else {
            NtfsWrapper ntfsWrapper = new NtfsWrapper();
            streamsList.addAll(ntfsWrapper.getStreams(cmdPath, null, null));
        }

        StringBuilder resultReport = new StringBuilder();
        resultReport.append(String.format("%nresult info for path=%s size=%d:", cmdPath, streamsList.size()));

        // https://github.com/vdmeer/asciitable?tab=readme-ov-file#gradle-grails
        // https://www.javatips.net/api/asciitable-master/src/test/java/de/vandermeer/asciitable/examples/AT_03_AlignmentOptions.java
        AsciiTable table = new AsciiTable();
        CWC_FixedWidth cwcFixed = new CWC_FixedWidth();
        cwcFixed.add(30).add(30).add(30).add(15);
        AT_Renderer tableRender = AT_Renderer.create().setCWC(cwcFixed);
        table.setRenderer(tableRender);
        AT_Row row;

        table.addRule();
        row = table.addRow("folder", "file", "stream", "length");
        row.getCells().get(0).getContext().setTextAlignment(TextAlignment.CENTER);
        row.getCells().get(1).getContext().setTextAlignment(TextAlignment.CENTER);
        row.getCells().get(2).getContext().setTextAlignment(TextAlignment.CENTER);
        row.getCells().get(3).getContext().setTextAlignment(TextAlignment.CENTER);
        table.addRule();
        for (StreamInfo dat : streamsList) {
            row = table.addRow(
                    dat.getFolderName() == null ? "" : dat.getFolderName(),
                    dat.getFileName(),
                    dat.getStreamName() == null ? "" : dat.getStreamName(),
                    humanReadableByteCountBin(dat.getStreamLength()));
            row.getCells().get(0).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(1).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(2).getContext().setTextAlignment(TextAlignment.RIGHT);
            row.getCells().get(3).getContext().setTextAlignment(TextAlignment.RIGHT);
            table.addRule();
        }


        resultReport.append("\n").append(table.render());

        log.debug("info:{}", resultReport.toString());

        //use case:
        // вывод данных об одном файле
        //-path='D:\INS\\demo-ntfs-file-streams\bravo.txt'
        //fileStreamNTFS.getStreams(null,null);
        // вывод данных о папке (и ADS в папке) + о всех вложенных файлах-папках
        //-path='D:\INS\demo-ntfs-file-streams\sub1
    }

    private void processTestFixWork(String cmdPath) {
        log.warn("processTestFixWork beg. cmdPath={}", cmdPath);
        Path datPath;
        datPath = Paths.get(cmdPath, "downloaded-from-internet.pdf"); //resp=application/pdf
        //datPath=Paths.get(cmdPath,"Zone-Identifier-one.bin"); //resp=text/plain
        if (!Files.exists(datPath)) {
            String msg = "file not exist [" + datPath.toAbsolutePath().toString() + "]";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }
        Tika tika = new Tika();
        try {
            String resp = tika.detect(datPath);
            log.debug("resp={}", resp);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.warn("processTestFixWork end");
    }

    private void processFile(String basePath, List<StreamInfo> streamsList, String xfile) throws IOException {
        log.debug("iteration. base={}. file={}", basePath, xfile);
        NtfsWrapper ntfsWrapper = new NtfsWrapper();
        List<StreamInfo> stm = ntfsWrapper.getStreams(basePath, null, xfile);
        streamsList.addAll(stm);
    }
}
