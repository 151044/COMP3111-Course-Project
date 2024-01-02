package hk.ust.comp3111.ui;

import javax.swing.*;
import java.awt.*;

/**
 * A JPanel with an associated preferred GridBagConstraint.
 */
public class ConstrainedPanel extends JPanel {
    /**
     * Gets the preferred GridBagConstraint for this ConstrainedPanel.
     * @return The grid bag constraint; by default, returns {@link UIHelper#defaultGridConstraints()}
     * but with {@link GridBagConstraints#fill} set to {@link GridBagConstraints#NONE}.
     */
    public GridBagConstraints getConstraint() {
        GridBagConstraints cons = UIHelper.defaultGridConstraints();
        cons.fill = GridBagConstraints.NONE;
        return cons;
    }
}
