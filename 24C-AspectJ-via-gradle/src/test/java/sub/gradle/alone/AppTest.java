/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package sub.gradle.alone;

import mil.teng.q2024.sub.gradle.alone.App;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AppTest {
    @Test void appHasAGreeting() {
        App classUnderTest = new App();
        assertNotNull(classUnderTest.getGreeting(), "app should have a greeting");
    }
}
