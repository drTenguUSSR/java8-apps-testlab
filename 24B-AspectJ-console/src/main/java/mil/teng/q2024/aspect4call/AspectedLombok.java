package mil.teng.q2024.aspect4call;

import lombok.Data;
import lombok.NonNull;

@Data
public class AspectedLombok {
        @NonNull
        private String nameOne;
        @NonNull
        private String nameTwo;
        private int dataLen;

}
