package mil.teng251.smtpsender.temp;

import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.WinBase;
import com.sun.jna.platform.win32.WinNT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

/**
 * for IDE run: add VM options:
 * -Dlog4j2.configurationFile=config/log4j2.xml
 */
public class FileStreamNTFS {
    private static final Logger logger = LoggerFactory.getLogger(FileStreamNTFS.class);

    public static void main(String[] args) {
        logger.warn("FileStreamNTFS - beg/2");
        //String path = "D:\\INS\\demo-ntfs-file-streams\\simple.txt";
        String path = "D:\\INS\\demo-ntfs-file-streams\\bravo.txt";
        dumpStreams(path);
        logger.warn("FileStreamNTFS - end");
    }

    private static void dumpStreams(String path) {
        Kernel32 kernel32;
        NtOsKrnl ntOsKrnl;
        try {
            kernel32 = Kernel32.INSTANCE;
            ntOsKrnl = NtOsKrnl.INSTANCE;
        } catch (Throwable t) {
            logger.warn("Throwable {}", t);
            return;
        }

        try {
            //String name = "\\\\?\\" + path;
            String name = path;
            WinNT.HANDLE handle = kernel32.CreateFile(name, 0, NtOsKrnl.FILE_SHARE_ALL, null, WinNT.OPEN_EXISTING, WinNT.FILE_FLAG_BACKUP_SEMANTICS, null);
            if (handle == WinBase.INVALID_HANDLE_VALUE) {
                throw new RuntimeException("CreateFile(" + path + "): 0x" + Integer.toHexString(kernel32.GetLastError()));
            }
            logger.debug("win32-handle={}",handle);
//            NtOsKrnl.FILE_CASE_SENSITIVE_INFORMATION_P fileInformation = new NtOsKrnl.FILE_CASE_SENSITIVE_INFORMATION_P();
//            int result = ntOsKrnl.NtQueryInformationFile(
//                    handle,
//                    new NtOsKrnl.IO_STATUS_BLOCK_P(),
//                    fileInformation,
//                    fileInformation.size(),
//                    NtOsKrnl.FileCaseSensitiveInformation);

            NtOsKrnl.FILE_STREAM_INFORMATION_P fileInformation = new NtOsKrnl.FILE_STREAM_INFORMATION_P();
            int result = ntOsKrnl.NtQueryInformationFile(
                    handle,
                    new NtOsKrnl.IO_STATUS_BLOCK_P(),
                    fileInformation,
                    fileInformation.size(),
                    NtOsKrnl.FileStreamInformation);

            kernel32.CloseHandle(handle);
            logger.debug("result={}",result);
            logger.debug("fileInfo.NextEntryOffset={}",fileInformation.NextEntryOffset);
            logger.debug("fileInfo.StreamNameLength={}",fileInformation.StreamNameLength);
            logger.debug("fileInfo.StreamSize={}",fileInformation.StreamSize);
            logger.debug("fileInfo.StreamAllocationSize={}",fileInformation.StreamAllocationSize);
            logger.debug("fileInfo.StreamName={}",fileInformation.StreamName);


            if (result != 0) {
                // https://docs.microsoft.com/en-us/openspecs/windows_protocols/ms-erref/596a1078-e883-4972-9bbc-49e60bebca55
                if (logger.isDebugEnabled())
                    logger.debug("NtQueryInformationFile(" + path + "): 0x" + Integer.toHexString(result));
            }
//            else if (fileInformation.Flags == 0) {
//                return FileAttributes.CaseSensitivity.INSENSITIVE;
//            }
//            else if (fileInformation.Flags == 1) {
//                return FileAttributes.CaseSensitivity.SENSITIVE;
//            }
            else {
                logger.warn("NtQueryInformationFile(" + path + "): unexpected {}",result);
            }
        } catch (Throwable t) {
            logger.warn("path: " + path, t);
        }

        return;
//one,two:fileInfo-1=60129542184,fileInfo-2=43
//one,two,three:     60129542184,fileInfo-2=58

    }


    //read data
    //https://github.com/codelibs/jcifs/blob/master/src/main/java/jcifs/smb1/smb1/Trans2FindFirst2Response.java
    private static final boolean useUnicode=true;
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
