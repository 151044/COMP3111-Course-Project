package hk.ust.comp3111.ui;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.function.Consumer;

/**
 * An implementation of AbstractAction to aid in implementing key bindings.
 */
public class KeyAction extends AbstractAction {
    private final Consumer<String> cons;
    private final String type;

    /**
     * Constructs a new KeyAction which runs the given {@link Consumer} on calling {@link AbstractAction#actionPerformed(ActionEvent)}.
     * @param type The type of action to run
     * @param cons The Runnable to run
     */
    public KeyAction(String type, Consumer<String> cons) {
        this.cons = cons;
        this.type = type;
    }

    @Override
    public void actionPerformed(ActionEvent ignored) {
        cons.accept(type);
    }
}
