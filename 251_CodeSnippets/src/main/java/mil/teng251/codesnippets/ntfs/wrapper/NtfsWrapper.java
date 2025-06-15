package mil.teng251.codesnippets.ntfs.wrapper;

import com.sun.jna.Memory;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.ptr.IntByReference;
import lombok.extern.slf4j.Slf4j;
import mil.teng251.codesnippets.ntfs.StreamInfo;

import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class NtfsWrapper {
    private static final boolean useUnicode = true;
    private static final DateTimeFormatter TEMP_FILENAME_DTM = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss.SSS");

    private static final Pattern STREAM_NAME_LOOKUP = Pattern.compile("^:(.*?):\\$DATA$");

    private static void dumpBufferToBinFile(Memory buffer, int bufferUse, String prefix, String suffix) throws IOException {
        String name = prefix + LocalDateTime.now().format(TEMP_FILENAME_DTM) + "-";
        Path tempFile = Files.createTempFile(name, suffix == null ? ".bin" : suffix);
        log.debug("dumpBuffer: bufferUse={} bytes. tempFile={}", bufferUse, tempFile);
        byte[] dats = new byte[bufferUse];
        buffer.read(0, dats, 0, bufferUse);
        try (FileOutputStream stream = new FileOutputStream(tempFile.toString())) {
            stream.write(dats);
        }
    }

    /**
     * based on com.sun.jna.platform.win32.Kernel32Test
     * https://github.com/java-native-access/jna/blob/master/contrib/platform/test/com/sun/jna/platform/win32/Kernel32Test.java
     *
     * @param filePath
     * @throws IOException
     */
    public static void readFixStream(String filePath) throws IOException {
        log.info("load file {}", filePath);
        WinNT.HANDLE handle = null;

        try {
            handle = Kernel32.INSTANCE.CreateFile(filePath
                    , WinNT.GENERIC_READ
                    , WinNT.FILE_SHARE_READ,
                    new WinBase.SECURITY_ATTRIBUTES()
                    , WinNT.OPEN_EXISTING
                    , WinNT.FILE_ATTRIBUTE_NORMAL
                    , null);
            if (handle == WinBase.INVALID_HANDLE_VALUE) {
                throw new IOException("CreateFile(" + filePath + "): error 0x" + Integer.toHexString(Kernel32.INSTANCE.GetLastError()));
            }
            ByteArrayOutputStream fileData = new ByteArrayOutputStream();
            byte[] readBuffer = new byte[2500];
            IntByReference lpNumberOfBytesRead = new IntByReference(0);
            int callCounter = 0;
            int readBytes;
            do {
                callCounter++;
                boolean readStatus = Kernel32.INSTANCE.ReadFile(handle, readBuffer, readBuffer.length, lpNumberOfBytesRead, null);
                if (!readStatus) {
                    String errCode = Integer.toHexString(Kernel32.INSTANCE.GetLastError());
                    String msg = "error reading " + " with code=" + errCode;
                    log.error(msg);
                    throw new IOException(msg);
                }
                readBytes = lpNumberOfBytesRead.getValue();
                log.debug("readed: callCounter={}. {} bytes", callCounter, readBytes);
                if (readBytes > 0) {
                    fileData.write(readBuffer, 0, readBytes);
                }
            } while (callCounter < 100 && readBytes != 0);

            if (callCounter == 100) {
                throw new RuntimeException("too many reads");
            }
            byte[] allFileData = fileData.toByteArray();
            String resultStr = new String(allFileData, 0, allFileData.length);
            log.debug("resultStr=[\n{}\n]", resultStr);
        } finally {
            if (handle != null && handle != WinBase.INVALID_HANDLE_VALUE) {
                Kernel32.INSTANCE.CloseHandle(handle);
            }
        }
        log.info("!done");
    }

    public String dropPathSeparator(String basePath) {
        if (basePath.endsWith("\\") || basePath.endsWith("/")) {
            return basePath.substring(0, basePath.length() - 1);
        }
        return basePath;
    }

    public List<StreamInfo> getStreams(String basePath, String subPath, String fileName) throws IOException {
        List<StreamInfo> resList = new ArrayList<>();
        WinNT.HANDLE handle = null;
        String filePath = "\\\\?\\" + dropPathSeparator(basePath) + (subPath == null ? "" : "\\" + subPath)
                + (fileName == null ? "" : "\\" + fileName);

        log.debug("work on [{}]", filePath);
        try {
            handle = Kernel32.INSTANCE.CreateFile(filePath
                    , WinNT.GENERIC_READ
                    , NtOsKrnl.FILE_SHARE_ALL
                    , null
                    , WinNT.OPEN_EXISTING
                    , WinNT.FILE_FLAG_BACKUP_SEMANTICS // or 0?
                    , null);
            if (handle == WinBase.INVALID_HANDLE_VALUE) {
                throw new IOException("CreateFile(" + filePath + "): error 0x" + Integer.toHexString(Kernel32.INSTANCE.GetLastError()));
            }
            log.debug("win32-handle={}", handle);

            int bufferSize = 256 * 1024;
            Memory buffer = new Memory(bufferSize);
            NtOsKrnl.IoStatusBlock ioStatus = new NtOsKrnl.IoStatusBlock();

            int resultNtQuery = NtOsKrnl.INSTANCE.NtQueryInformationFile(
                    handle,
                    ioStatus,
                    buffer,
                    buffer.size(),
                    NtOsKrnl.NTQUERYINFORMATIONFILE_FILESTREAMINFORMATION);

            if (log.isDebugEnabled()) {
                log.debug("resultNtQuery={} ioStatus.status={} ioStatus.Information={}"
                        , String.format("0x%H", resultNtQuery)
                        , String.format("0x%H", ioStatus.Status)
                        , ioStatus.Information.intValue()
                );
            }

            //dumpBufferToBinFile(buffer, ioStatus.Information.intValue(), "dump-", ".bin");

            if (resultNtQuery != 0) {
                String msg = "Failed to load info about [" + filePath + "]."
                        + " buffer-size=" + bufferSize
                        + " resultNtQuery=0x" + Integer.toHexString(resultNtQuery)
                        + "\nsee detail:"
                        + "\n\thttps://docs.microsoft.com/en-us/openspecs/windows_protocols/ms-erref/596a1078-e883-4972-9bbc-49e60bebca55"
                        + "\n\thttps://joyasystems.com/list-of-ntstatus-codes";
                log.error(msg);
                throw new IllegalStateException(msg);
                // resultNtQuery == NtOsKrnl.STATUS_INFO_LENGTH_MISMATCH || resultNtQuery == NtOsKrnl.STATUS_BUFFER_OVERFLOW
                // Increase the buffer size and try again
                //bufferSize *= 2;
                //buffer = new Memory(bufferSize);
                //continue;
            }

            long offset = 0;
            while (offset < ioStatus.Information.longValue()) {
                NtOsKrnl.FileStreamFullInfo fileInfo = new NtOsKrnl.FileStreamFullInfo();
                fileInfo.load(buffer.share(offset));
                log.debug("offset={}. streamName.len={} bytes. nextOffset={}", offset, fileInfo.StreamNameLength, fileInfo.NextEntryOffset);

                if (fileInfo.StreamNameLength > 0) {
                    // Convert char[] to String, considering UTF-16 encoding
                    String streamName = new String(fileInfo.StreamName, 0, fileInfo.StreamNameLength / 2);
                    //stream with name='::$DATA' - main data
                    if (streamName.equals("::$DATA")) {
                        streamName = null;
                    } else {
                        //streamName=":one:$DATA"
                        Matcher matcher = STREAM_NAME_LOOKUP.matcher(streamName);
                        if (!matcher.find()) {
                            throw new IllegalStateException("failed decode stream name from [" + streamName + "]");
                        }
                        streamName = matcher.group(1);
                    }
                    log.debug("stream: name={} length={}", streamName, fileInfo.StreamSize);
                    StreamInfo streamInfo = new StreamInfo(subPath, fileName, streamName, fileInfo.StreamSize.getValue()
                            , null, null);
                    resList.add(streamInfo);
                }

                if (fileInfo.NextEntryOffset == 0) {
                    log.debug("fileInfo.NextEntryOffset is 0. break");
                    break;
                }

                offset += fileInfo.NextEntryOffset;
                log.debug("next-offset={}", offset);
            }
        } finally {
            if (handle != null && handle != WinBase.INVALID_HANDLE_VALUE) {
                Kernel32.INSTANCE.CloseHandle(handle);
            }
        }
        log.info("job done! resList.size={}", resList.size());
        return resList;
    }
}
