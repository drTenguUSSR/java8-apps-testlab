package mil.teng251.codesnippets.ntfs;

import com.sun.jna.Memory;
import com.sun.jna.Native;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinNT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FileStreamNTFS {
    private static final Logger logger = LoggerFactory.getLogger(FileStreamNTFS.class);
    private static final boolean useUnicode = true;
    private static final DateTimeFormatter TEMP_FILENAME_DTM = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss.SSS");

    public static void dumpStreams(String path) {
        WinNT.HANDLE handle = null;
        try {
            String name = "\\\\?\\" + path;
            //String name = path;
            handle = Kernel32.INSTANCE.CreateFile(name
                    , WinNT.GENERIC_READ
                    , NtOsKrnl.FILE_SHARE_ALL
                    , null
                    , WinNT.OPEN_EXISTING
                    , WinNT.FILE_FLAG_BACKUP_SEMANTICS // or 0?
                    , null);
            if (handle == WinBase.INVALID_HANDLE_VALUE) {
                throw new RuntimeException("CreateFile(" + path + "): 0x" + Integer.toHexString(Kernel32.INSTANCE.GetLastError()));
            }
            logger.debug("win32-handle={}", handle);

            int bufferSize = 1024;
            Memory buffer = new Memory(bufferSize);
            NtOsKrnl.IoStatusBlock ioStatus = new NtOsKrnl.IoStatusBlock();

            int resultNtQuery = NtOsKrnl.INSTANCE.NtQueryInformationFile(
                    handle,
                    ioStatus,
                    buffer,
                    buffer.size(),
                    NtOsKrnl.NTQUERYINFORMATIONFILE_FILESTREAMINFORMATION);

            logger.debug("resultNtQuery={} ioStatus.status={} ioStatus.Information={} ptr.size={} long.size={}"
                    , String.format("0x%H", resultNtQuery)
                    , String.format("0x%H", ioStatus.Status)
                    , ioStatus.Information.intValue()
                    , Native.POINTER_SIZE
                    , WinDef.LONG.SIZE
            );

            dumpBufferToBinFile(buffer, ioStatus.Information.intValue(), "dump-", ".bin");


//            if (resultNtQuery == NtOsKrnl.STATUS_INFO_LENGTH_MISMATCH || resultNtQuery == NtOsKrnl.STATUS_BUFFER_OVERFLOW) {
//                // Increase the buffer size and try again
//                bufferSize *= 2;
//                buffer = new Memory(bufferSize);
//                continue;
//            }

            if (resultNtQuery != 0) {
                logger.debug("resultNtQuery: NtQueryInformationFile({}): 0x{}", path, Integer.toHexString(resultNtQuery));
                return;
            }
            long offset = 0;
            logger.debug("fileInfo.size={}", (new NtOsKrnl.FileStreamFullInfo()).size());

            while (offset < ioStatus.Information.longValue()) {
                NtOsKrnl.FileStreamFullInfo fileInfo = new NtOsKrnl.FileStreamFullInfo();
                fileInfo.load(buffer.share(offset));
                logger.debug("offset={}. streamName.len={} bytes. nextOffset={}", offset, fileInfo.StreamNameLength, fileInfo.NextEntryOffset);

// add read stream flags
// https://learn.microsoft.com/en-us/windows-hardware/drivers/ddi/ntifs/ns-ntifs-_file_stream_information
//                int streamNameLength = buffer.getShort(offset + 0) & 0xFFFF; // Length of the stream name
//                int streamSize = buffer.getInt(offset + 8); // Size of the stream
//                int streamFlags = buffer.getInt(offset + 12); // Flags

                if (fileInfo.StreamNameLength > 0) {
                    // Convert char[] to String, considering UTF-16 encoding
                    String strData = "name:'" + new String(fileInfo.StreamName, 0, fileInfo.StreamNameLength / 2) + "'"
                            + " , data-length:" + fileInfo.StreamSize;
                    //stream with name='::$DATA' - main data
                    logger.debug("stream: {}", strData.trim());
                }

                if (fileInfo.NextEntryOffset == 0) {
                    logger.debug("fileInfo.NextEntryOffset is 0. break");
                    break;
                }

                offset += fileInfo.NextEntryOffset;
                logger.debug("next-offset={}", offset);
            }

        } catch (Throwable t) {
            logger.warn("path: " + path, t);
        } finally {
            if (handle != null && !handle.equals(WinBase.INVALID_HANDLE_VALUE)) {
                Kernel32.INSTANCE.CloseHandle(handle);
            }
        }

        return;
//one,two:fileInfo-1=60129542184,fileInfo-2=43
//one,two,three:     60129542184,fileInfo-2=58

    }

    private static void dumpBufferToBinFile(Memory buffer, int bufferUse, String prefix, String suffix) throws IOException {
        String name = prefix + LocalDateTime.now().format(TEMP_FILENAME_DTM) + "-";
        Path tempFile = Files.createTempFile(name, suffix == null ? ".bin" : suffix);
        logger.debug("dumpBuffer: bufferUse={} bytes. tempFile={}", bufferUse, tempFile);
        byte[] dats = new byte[bufferUse];
        buffer.read(0, dats, 0, bufferUse);
        try (FileOutputStream stream = new FileOutputStream(tempFile.toString())) {
            stream.write(dats);
        }
    }


    String readString(byte[] src, int srcIndex, int len) {
        String str = null;
        if (useUnicode) {
            // should Unicode alignment be corrected for here?
            str = new String(src, srcIndex, len, StandardCharsets.UTF_8);
        } else {

            /* On NT without Unicode the fileNameLength
             * includes the '\0' whereas on win98 it doesn't. I
             * guess most clients only support non-unicode so
             * they don't run into this.
             */

            /* UPDATE: Maybe not! Could this be a Unicode alignment issue. I hope
             * so. We cannot just comment out this method and use readString of
             * ServerMessageBlock.java because the arguments are different, however
             * one might be able to reduce this.
             */

            if (len > 0 && src[srcIndex + len - 1] == '\0') {
                len--;
            }
            str = new String(src, srcIndex, len, StandardCharsets.UTF_8);
        }
        return str;
    }


}
