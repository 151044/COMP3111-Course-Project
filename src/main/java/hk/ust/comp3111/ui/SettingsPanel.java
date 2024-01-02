package hk.ust.comp3111.ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatIntelliJLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import hk.ust.comp3111.Main;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A panel for choosing different themes.
 */
public class SettingsPanel extends ConstrainedPanel {
    private static final Map<String, LookAndFeel> LOOK_AND_FEEL = new HashMap<>(
            Map.of("Nimbus", new NimbusLookAndFeel(),
                    "Light", new FlatLightLaf(),
                    "Dark", new FlatDarkLaf(),
                    "Light IntelliJ", new FlatIntelliJLaf(),
                    "Dark IntelliJ", new FlatDarculaLaf(),
                    "Light MacOS", new FlatMacLightLaf(),
                    "Dark MacOS", new FlatMacDarkLaf()));

    private static final List<String> THEME_ORDER =
            new ArrayList<>(
                    List.of("Nimbus", "Light", "Dark", "Light IntelliJ", "Dark IntelliJ", "Light MacOS", "Dark MacOS"));
    private final JPanel themeGroup;

    /**
     * Constructs a new theme chooser panel.
     */
    public SettingsPanel() {
        setLayout(new GridBagLayout());
        // Themes
        themeGroup = new JPanel();
        themeGroup.setLayout(new BoxLayout(themeGroup, BoxLayout.Y_AXIS));
        ButtonGroup group = new ButtonGroup();
        for (String s : THEME_ORDER) {
            JRadioButton button = new JRadioButton(s);
            group.add(button);
            themeGroup.add(button);
            if (LOOK_AND_FEEL.entrySet().stream()
                    .filter(ent -> ent.getValue().getClass() == UIManager.getLookAndFeel().getClass())
                    .map(Map.Entry::getKey).findFirst().orElse("").equals(s)) {
                button.setSelected(true);
            }
            button.addActionListener(ignored -> {
                try {
                    ThemeManager.setLookAndFeel(LOOK_AND_FEEL.get(s));
                } catch (UnsupportedLookAndFeelException e) {
                    UIHelper.showErrorDialog("Theme " + s + " is not supported!");
                }
            });
        }
        themeGroup.setBorder(new TitledBorder("Themes"));
        // Back
        JButton backButton = new JButton("Back");
        backButton.addActionListener(ignored -> Main.getFrame().setPanel(new StartPanel()));

        // Constraints and add
        GridBagConstraints cons = UIHelper.defaultGridConstraints();
        cons.gridheight = 1;
        cons.fill = GridBagConstraints.HORIZONTAL;
        cons.weighty = 0.7;
        add(themeGroup, cons);
        cons.weighty = 0.3;
        cons.gridy++;
        add(backButton, cons);
    }
}
