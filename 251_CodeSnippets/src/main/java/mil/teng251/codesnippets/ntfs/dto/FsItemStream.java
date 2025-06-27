package mil.teng251.codesnippets.ntfs.dto;

import lombok.Value;

/**
 * информация о NTFS-потоке для выбранного {@see FsItem}
 */
@Value
public class FsItemStream {
    FsItem ref;
    String name;
    long streamLength;
    String report;
}
