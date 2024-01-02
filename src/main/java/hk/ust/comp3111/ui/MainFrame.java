package hk.ust.comp3111.ui;

import javax.swing.*;
import java.awt.*;

/**
 * The main application frame.
 */
public class MainFrame extends JFrame {
    private ConstrainedPanel current = new StartPanel();
    private final JPanel container = new JPanel();
    /**
     * Constructs a new application frame.
     */
    public MainFrame() {
        super("Main Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        container.setLayout(new GridBagLayout());
        container.add(current, current.getConstraint());
        add(container, BorderLayout.CENTER);
        pack();
        setSize(600, 600);
        setPreferredSize(new Dimension(600, 600));
        setLocationRelativeTo(null);
    }

    /**
     * Sets the currently displayed panel.
     * @param panel The panel to show
     */
    public void setPanel(ConstrainedPanel panel) {
        container.remove(current);
        current = panel;
        container.add(panel, panel.getConstraint());
        pack();
        Dimension dim = new Dimension(
                (int) Math.max(panel.getPreferredSize().getWidth(), 600),
                (int) Math.max(panel.getPreferredSize().getHeight(), 600)
        );
        setSize(dim);
        setPreferredSize(dim);
        repaint();
        setLocationRelativeTo(null);
    }
}
