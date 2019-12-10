package chatBoxServer;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ChatServerTest {

    @Test
    void runTest() {
        int[] numbers = {0, 1, 2, 3, 4};
        assertAll("numbers",
                () -> assertEquals(numbers[0], 0),
                () -> assertEquals(numbers[3], 3),
                () -> assertEquals(numbers[4], 4)
        );
    }

    @Test
    void runAnotherTest() {
        int a = 1 + 2;
        assertEquals(3, a);
    }
}