package mil.teng24b.aspectj.alib;

import lombok.Data;
import lombok.NonNull;

@Data
public class AnimalLombok {
    @NonNull
    private String nameOne;
    @NonNull
    private String nameTwo;
    private int dataLen;
}
