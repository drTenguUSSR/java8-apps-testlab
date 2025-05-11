package mil.teng251.codesnippets.ntfs;

import com.google.common.base.Strings;
import lombok.extern.slf4j.Slf4j;
import mil.teng251.codesnippets.App;
import mil.teng251.codesnippets.SnipExec;
import org.apache.commons.cli.CommandLine;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
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
public class NtfsStreamsInfo implements SnipExec {

    @Override
    public void execute(CommandLine commandLine) throws IOException {
        String cmdPath = commandLine.getOptionValue("path");
        if (Strings.isNullOrEmpty(cmdPath)) {
            throw new IllegalArgumentException("param '-path' is null or empty!");
        }
        boolean cmdValidateInternet = commandLine.hasOption(App.NTFS_VALIDATE_INTERNET_DOWNLOAD);
        int cmdAdsLimit = Integer.parseInt(commandLine.getOptionValue(App.NTFS_LOAD_ADS_LIMIT, "0"));
        log.debug("cmdPath={} validateInternet={} cmdAdsLimit={}", cmdPath, cmdValidateInternet, cmdAdsLimit);

        Path paramPath = Paths.get(cmdPath);
        if (!Files.exists(paramPath)) {
            throw new IllegalArgumentException("path [" + cmdPath + "] not exist");
        }
        boolean isDirectory = Files.isDirectory(paramPath);
        log.debug("isDirectory={}", isDirectory);

        FileStreamNTFS fileStreamNTFS = new FileStreamNTFS(cmdPath);
        List<StreamInfo> streamsList = new ArrayList<>();
        if (isDirectory) {
            Set<String> allFiles = new HashSet<>();
            try (Stream<Path> streamItem = Files.list(paramPath)) {
                allFiles = streamItem
                        .filter(Files::isRegularFile)
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .collect(Collectors.toSet());
            }
            for (String xfile : allFiles) {
                processFile(fileStreamNTFS, streamsList, xfile);
            }
        } else {
            streamsList.addAll(fileStreamNTFS.getStreams(null, null));
        }

        log.debug("result info for path={} size={}: [", cmdPath, streamsList.size());
        for (StreamInfo dat : streamsList) {
            log.debug("- {},{}:{} len={}", dat.getFolderName(), dat.getFileName(), dat.getStreamName(), dat.getStreamLength());
        }
        log.debug("]");

        //use case:
        // вывод данных об одном файле
        //-path='D:\INS\\demo-ntfs-file-streams\bravo.txt'
        //fileStreamNTFS.getStreams(null,null);
        // вывод данных о папке (и ADS в папке) + о всех вложенных файлах-папках
        //-path='D:\INS\demo-ntfs-file-streams\sub1


        //String path = "D:\\INS\\demo-ntfs-file-streams\\simple.txt";
        //String path = "D:\\INS\\demo-ntfs-file-streams\\bravo.txt";
        //String path = "D:\\INS\\demo-ntfs-file-streams\\FirefoxPortable32-70.0.1.zip";
        //String path = "D:\\INS\\demo-ntfs-file-streams\\downloaded-from-internet.pdf";
        // downloaded-from-internet.pdf:Zone.Identifier
        //String pathBase = args[1];

//        log.debug("show steams for pathBase={} toFile={}",pathBase,path);
//        List<StreamInfo> streamsList = FileStreamNTFS.getStreams(path);
//        log.debug("res: {}",streamsList);
        //FileStreamNTFS.readFixStream("D:\\INS\\demo-ntfs-file-streams\\simple-long-data-file.txt");
    }

    private void processFile(FileStreamNTFS fileStreamNTFS, List<StreamInfo> streamsList, String xfile) throws IOException {
        log.debug("iteration. base={}. file={}", fileStreamNTFS.getBasePath(), xfile);
        List<StreamInfo> stm = fileStreamNTFS.getStreams(null, xfile);
        streamsList.addAll(stm);
    }


}
