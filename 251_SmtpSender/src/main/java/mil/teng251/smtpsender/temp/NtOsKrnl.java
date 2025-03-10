package mil.teng251.smtpsender.temp;


import com.google.common.primitives.UnsignedLong;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;

@SuppressWarnings("SpellCheckingInspection")
interface NtOsKrnl extends StdCallLibrary, WinNT {
    NtOsKrnl INSTANCE = Native.load("NtDll", NtOsKrnl.class, W32APIOptions.UNICODE_OPTIONS);

    int FILE_SHARE_ALL = FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE;

    //--------------------------------
//https://learn.microsoft.com/ru-ru/windows-hardware/drivers/ddi/ntifs/nf-ntifs-ntqueryinformationfile
    int FileCaseSensitiveInformation = 71;
    int FileStreamInformation = 22;
    int MAX_PATH = 260;
    //com.google.common.primitives.UnsignedLong x1;
    //UnsignedLong x2;
    com.google.common.primitives.UnsignedLong m2 = UnsignedLong.valueOf(3L);

    int NtQueryInformationFile(
            HANDLE FileHandle,
            IO_STATUS_BLOCK_P ioStatusBlock,
            Structure fileInformation,
            long length,
            int fileInformationClass);

    //--------------------------------
//https://learn.microsoft.com/ru-ru/windows-hardware/drivers/ddi/ntifs/ns-ntifs-_file_stream_information
//  typedef struct _FILE_STREAM_INFORMATION {
//        ULONG         NextEntryOffset;
//        ULONG         StreamNameLength;
//        LARGE_INTEGER StreamSize;
//        LARGE_INTEGER StreamAllocationSize;
//        WCHAR         StreamName[1];
//    } FILE_STREAM_INFORMATION, *PFILE_STREAM_INFORMATION;
    //LARGE_INTEGER - Represents a 64-bit signed integer value.
    //https://stackoverflow.com/questions/384502/what-is-the-bit-size-of-long-on-64-bit-windows#:~:text=The%20size%20of%20long%20on,32%20bits%20(4%20bytes).
    //ULONG - 32 bit | 32 bit
    //https://bluedesk.blogspot.com/2022/07/sizes-of-windows-integral-types.html
    //ULONG - 32 bit (4 byte)
    //LARGE_INTEGER- 64 bit (8 byte)

//Delphi: type _FILE_STREAM_INFORMATION = record
//    NextEntryOffset: Cardinal;
//    StreamNameLength: Cardinal;
//    StreamSize: int64;
//    StreamAllocationSize: int64;
//    StreamName: array [0 .. MAX_PATH] of WideChar;
//    end;

    @Structure.FieldOrder({"Pointer", "Information"})
    final
    class IO_STATUS_BLOCK_P extends Structure implements Structure.ByReference {
        public com.sun.jna.Pointer Pointer;
        public Pointer Information;
    }

    @Structure.FieldOrder("Flags")
    final
    class FILE_CASE_SENSITIVE_INFORMATION_P extends Structure implements Structure.ByReference {
        // initialize with something crazy to make sure the native call did write 0 or 1 to this field
        public long Flags = 0xFFFF_FFFFL;  // FILE_CS_FLAG_CASE_SENSITIVE_DIR = 1
    }

    //?? ULONG -> long | UnsignedLong ?
    //public UnsignedLong NextEntryOffset;
    //public UnsignedLong StreamNameLength;
    //public long NextEntryOffset = 0xFFFF_FFFFL;
    //public long StreamNameLength = 0xFFFF_FFFFL;
    @Structure.FieldOrder({"NextEntryOffset", "StreamNameLength", "StreamSize", "StreamAllocationSize", "StreamName"})
    final class FILE_STREAM_INFORMATION_P extends Structure implements Structure.ByReference {
        public int NextEntryOffset;
        public int StreamNameLength;
        public WinNT.LARGE_INTEGER StreamSize;
        public WinNT.LARGE_INTEGER StreamAllocationSize;
        public char[] StreamName = new char[MAX_PATH];
    }
}