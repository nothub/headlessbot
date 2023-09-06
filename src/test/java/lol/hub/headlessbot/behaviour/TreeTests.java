package lol.hub.headlessbot.behaviour;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TreeTests {
    @Test
    void run() {
        assertEquals(2147483646, Integer.sum(2147183646, 300000));
    }
}
