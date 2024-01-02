package hk.ust.comp3111.ui;

import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLightLaf;
import hk.ust.comp3111.reflect.Reflect;
import org.junit.jupiter.api.Test;

import javax.swing.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeFalse;

class SettingsPanelTest {
    JRadioButton getButton(JPanel panel, String str) {
        return Arrays.stream(panel.getComponents())
                .filter(c -> c instanceof JRadioButton)
                .map(c -> (JRadioButton) c)
                .filter(c -> c.getText().equals(str))
                .findFirst()
                .orElseThrow();
    }
    @Test
    void setTheme() throws UnsupportedLookAndFeelException, IllegalAccessException, NoSuchFieldException {
        ThemeManager.setLookAndFeel(new FlatLightLaf());
        // target function: new SettingsPanel()
        SettingsPanel panel = new SettingsPanel();
        JPanel themePanel = Reflect.getField("themeGroup", JPanel.class, panel);
        assertTrue(getButton(themePanel, "Light").isSelected());

        getButton(themePanel, "Dark IntelliJ").doClick();
        assertTrue(UIManager.getLookAndFeel() instanceof FlatDarculaLaf);
    }

    @SuppressWarnings("unchecked")
    @Test
    void setUnsupportedTheme() throws NoSuchFieldException, IllegalAccessException {
        assumeFalse(GraphicsEnvironment.isHeadless());
        Map<String, LookAndFeel> lafMap = Reflect.getStaticField("LOOK_AND_FEEL", Map.class, SettingsPanel.class);
        List<String> themeOrder = Reflect.getStaticField("THEME_ORDER", List.class, SettingsPanel.class);
        LookAndFeel noYouLookAndFeel = new LookAndFeel() {
            @Override
            public String getName() {
                return "No You";
            }

            @Override
            public String getID() {
                return "NoU";
            }

            @Override
            public String getDescription() {
                return "The No You Look and Feel, designed for maximum incompatibility!";
            }

            @Override
            public boolean isNativeLookAndFeel() {
                return false;
            }

            @Override
            public boolean isSupportedLookAndFeel() {
                return false;
            }
        };
        lafMap.put("No You", noYouLookAndFeel);
        themeOrder.add("No You");

        // target function: new SettingsPanel()
        SettingsPanel panel = new SettingsPanel();
        JPanel themePanel = Reflect.getField("themeGroup", JPanel.class, panel);

        getButton(themePanel, "No You").doClick();
        assertTrue(Arrays.stream(Window.getWindows())
                .anyMatch(w -> w instanceof JDialog));
        Arrays.stream(Window.getWindows()).forEach(Window::dispose);
    }
}