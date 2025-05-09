package mil.teng251.codesnippets.ntfs;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class StreamInfo {
    private String folderName;
    private String fileName;
    private String streamName;
    private long streamLength;
    private String streamData;
}
