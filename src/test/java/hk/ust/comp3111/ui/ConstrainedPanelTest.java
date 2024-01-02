package hk.ust.comp3111.ui;

import org.junit.jupiter.api.Test;

import java.awt.*;

import static org.junit.jupiter.api.Assertions.*;

class ConstrainedPanelTest {

    @Test
    void getConstraint() {
        ConstrainedPanel panel = new ConstrainedPanel();
        // target function: ConstrainedPanel.getConstraint()
        assertEquals(GridBagConstraints.NONE, panel.getConstraint().fill);
        // target function: ConstrainedPanel.getConstraint()
        assertNotEquals(UIHelper.defaultGridConstraints(), panel.getConstraint());
    }
}