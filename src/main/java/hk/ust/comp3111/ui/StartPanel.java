package hk.ust.comp3111.ui;

import hk.ust.comp3111.Main;
import hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.engine.Engine;
import hk.ust.comp3111.engine.ImagePath;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Optional;

/**
 * The starting panel for the game.
 */
public class StartPanel extends ConstrainedPanel {
    /**
     * Constructs a new starting panel.
     */
    public StartPanel() {
        setLayout(new GridBagLayout());
        GridBagConstraints cons = UIHelper.defaultGridConstraints();
        cons.gridheight = 1;
        cons.fill = GridBagConstraints.HORIZONTAL;

        JLabel title = new JLabel("Tom and Jerry Maze Game");
        Font largerFont = title.getFont().deriveFont(20.0f);
        title.setFont(largerFont);
        addAndIncrement(title, cons);

        JButton playButton = new JButton("Start Game");
        JButton editButton = new JButton("Edit Map");
        JButton shortestButton = new JButton("Shortest Path");
        JButton rulesButton = new JButton("Rules");
        JButton settingsButton = new JButton("Settings");
        JButton exitButton = new JButton("Exit");

        Font smallerFont = largerFont.deriveFont(15.0f);
        playButton.setFont(smallerFont);
        editButton.setFont(smallerFont);
        shortestButton.setFont(smallerFont);
        rulesButton.setFont(smallerFont);
        settingsButton.setFont(smallerFont);
        exitButton.setFont(smallerFont);

        playButton.addActionListener((ae) -> {
            Optional<File> f = UIHelper.showFileOpener();
            if (f.isPresent()) {
                File file = f.orElseThrow();
                try {
                    MapData data = new MapData(file.toPath());
                    BreadthFirstSearch bfs = new BreadthFirstSearch();
                    if (data.findExit().isEmpty() || data.findEntrance().isEmpty()
                        || bfs.bfsGeneral(data,
                            data.findEntrance().orElseThrow(),
                            data.findExit().orElseThrow(),
                            true).isEmpty()) {
                        UIHelper.showErrorDialog("The file is invalid!");
                        return;

                    }
                    Engine e = new Engine(data, ImagePath.IMAGES);
                    Main.getFrame().setPanel(new GamePanel(e));
                } catch (IOException e) {
                    UIHelper.showErrorDialog("Cannot read file!");
                }
            } else {
                UIHelper.showErrorDialog("Cannot open the file!");
            }
        });
        editButton.addActionListener(ignored -> Main.getFrame().setPanel(new MapCreatorPanel()));
        shortestButton.addActionListener(ignored -> {
            Optional<File> f = UIHelper.showFileOpener();
            if (f.isPresent()) {
                try {
                    Main.getFrame().setPanel(new ShortestPathPanel(new MapData(f.orElseThrow().toPath())));
                } catch (IOException e) {
                    UIHelper.showErrorDialog("An error has occurred.\n" + e.getMessage());
                }
            }
        });
        rulesButton.addActionListener(ignored -> Main.getFrame().setPanel(new RulesPanel()));
        settingsButton.addActionListener(ignored -> Main.getFrame().setPanel(new SettingsPanel()));
        exitButton.addActionListener(ignored -> System.exit(0));

        addAndIncrement(playButton, cons);
        addAndIncrement(editButton, cons);
        addAndIncrement(shortestButton, cons);
        addAndIncrement(rulesButton, cons);
        addAndIncrement(settingsButton, cons);
        addAndIncrement(exitButton, cons);
    }

    private void addAndIncrement(JComponent component, GridBagConstraints cons) {
        add(component, cons);
        cons.gridy++;
    }
}
