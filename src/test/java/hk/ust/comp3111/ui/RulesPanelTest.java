package hk.ust.comp3111.ui;

import org.junit.jupiter.api.Test;

import javax.swing.*;

import static org.junit.jupiter.api.Assertions.*;

class RulesPanelTest {
    @Test
    void newRulesPanel() {
        // target function: new RulesPanel()
        RulesPanel panel = new RulesPanel();
        assertInstanceOf(JScrollPane.class, panel.getComponent(0));
        assertInstanceOf(BoxLayout.class, panel.getLayout());
    }
}