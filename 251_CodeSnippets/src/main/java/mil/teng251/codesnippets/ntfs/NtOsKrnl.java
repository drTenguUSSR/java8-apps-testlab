package mil.teng251.codesnippets.ntfs;


import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;
import com.sun.jna.platform.win32.BaseTSD;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

import java.util.Arrays;
import java.util.List;

@SuppressWarnings("SpellCheckingInspection")
interface NtOsKrnl extends StdCallLibrary, WinNT {
    NtOsKrnl INSTANCE = Native.load("NtDll", NtOsKrnl.class, W32APIOptions.UNICODE_OPTIONS);

    int FILE_SHARE_ALL = FILE_SHARE_READ | FILE_SHARE_WRITE | FILE_SHARE_DELETE;

    //--------------------------------
//https://learn.microsoft.com/en-us/windows-hardware/drivers/ddi/ntifs/nf-ntifs-ntqueryinformationfile
    int FileCaseSensitiveInformation = 71;
    int FileStreamInformation = 22;
    // 22 = https://learn.microsoft.com/en-us/windows-hardware/drivers/ddi/ntifs/ns-ntifs-_file_stream_information
    int MAX_PATH = 260;

    int NtQueryInformationFile(
            HANDLE FileHandle,
            IoStatusBlock ioStatusBlock,
            Pointer fileInformation,
            long length,
            int fileInformationClass);

    //--------------------------------
//https://learn.microsoft.com/en-us/windows-hardware/drivers/ddi/ntifs/ns-ntifs-_file_stream_information
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


    @Structure.FieldOrder({"Status", "Information"})
    final
    class IoStatusBlock extends Structure implements Structure.ByReference {
        //public Pointer Pointer;
        //public Pointer Information;
        public WinNT.LARGE_INTEGER Status;
        public BaseTSD.ULONG_PTR Information;
    }

    class IoStatusBlock_M0 extends Structure implements Structure.ByReference {
        public Pointer Pointer;
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
    final class FileStreamFullInfo_M0 extends Structure implements Structure.ByReference {
        public int NextEntryOffset;
        public int StreamNameLength;
        public WinNT.LARGE_INTEGER StreamSize;
        public WinNT.LARGE_INTEGER StreamAllocationSize;
        public char[] StreamName = new char[MAX_PATH];
        public void load(Pointer p) {
            useMemory(p);
            read();
        }
    }


    public static class FileStreamFullInfo extends Structure {
        // Using char[] to represent wide-character strings (UTF-16)
        public char[] StreamName = new char[256]; // Adjust size as needed
        public int StreamNameLength;
        public long AllocationSize;
        public long EndOfFile;
        public int FileAttributes;

        @Override
        protected List<String> getFieldOrder() {
            return Arrays.asList("StreamName", "StreamNameLength", "AllocationSize", "EndOfFile", "FileAttributes");
        }

        // Custom constructor to initialize the structure from a memory address
        public FileStreamFullInfo(Pointer p) {
            useMemory(p);
            read();
        }
    }
}