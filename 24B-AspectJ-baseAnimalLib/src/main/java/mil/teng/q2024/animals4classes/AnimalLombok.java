package mil.teng.q2024.animals4classes;

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
