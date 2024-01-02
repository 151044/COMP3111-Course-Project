package hk.ust.comp3111.ui;

import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.*;

class KeyActionTest {
    @Test
    void newAction() {
        // target function: new KeyAction(String, Consumer<String>)
        KeyAction act = new KeyAction("abc", (ignored) -> assertEquals("abc", ignored));
        act.actionPerformed(null);
    }
    @Test
    void actionPerformed() {
        final AtomicBoolean bool = new AtomicBoolean(false);
        KeyAction act = new KeyAction("abc", (ignored) -> bool.set(true));
        // target function: KeyAction.actionPerformed(ActionEvent)
        act.actionPerformed(null);
        assertTrue(bool.get());
    }
}