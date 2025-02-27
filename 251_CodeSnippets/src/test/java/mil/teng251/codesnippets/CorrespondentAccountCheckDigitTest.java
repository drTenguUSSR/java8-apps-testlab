package mil.teng251.codesnippets;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CorrespondentAccountCheckDigitTest {

    @Test
    void replaceAccountKeycodeA() {
        String m1="30101810900000000746";
        String m2=CorrespondentAccountCheckDigit.replaceAccountKeycode(m1,"X");
        Assertions.assertEquals("30101810X00000000746",m2);
    }
}