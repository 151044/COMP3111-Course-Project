package hk.ust.comp3111.ui;

import hk.ust.comp3111.Main;

import javax.swing.*;
import java.awt.*;

/**
 * A panel for displaying the rules of the game.
 */
public class RulesPanel extends ConstrainedPanel {
    /**
     * The rules.
     * I did hardcode this because I can.
     */
    private static final String RULES = """
Welcome to Tom and Jerry Maze Game!
This is a game where you would have to escape the Maze!
                        
Gameplay:
You are Jerry, the mouse. You are stuck inside the maze, and have to escape it. From the Entrance Point (marked green), you'll have to find your way to the Exit Point (marked yellow) through the maze and escape.
However, it's not that easy! There is Tom, the cat that'll try its best to catch you. If Tom catches you, then you'll lose.
Don't worry though, as there is an item on the map that can help you. Introducing the Crystal, if Jerry manages to get the Crystal, which spawns randomly on the map, Jerry will temporarily gain magical powers that makes it invincible. Tom, upon seeing this, would find its way to the Entrance Point, hoping to avoid you! At this point, you can decide what to do. Do you race to the Exit Point without Tom on your tail? Or, do you chase Tom to give it a taste of its own medicine?
Beware though, the Crystal only lasts a certain amount of time, and when it runs out, the Crystal would respawn somewhere else, and Tom would chase after you again!
                        
Instructions:
The game runs with CSV files.
Start Map
When you click the Start Map button, the game will prompt you to select a file. Simply select a compatible CSV file and the game will start!
                        
Edit Map
If you don't have a CSV file, there is no need to worry about its creation. The Edit Map button does it for you!
Clicking on Edit Map takes you to a Maze Editor, and after creating your own map with the helper tools provided, you can save the CSV file on your computer, and select it when starting the game.
                        
Remember to include an Entrance Point and an Exit Point!
                        
Shortest Path
Providing a valid CSV file when prompted would show you the shortest path from the Entrance Point to the Exit Point.
                        
Settings
Themes!
                        
Credits:
Inspired by Tom and Jerry, created by William Hanna and Joseph Barbera.
""";
    /**
     * Constructs a new panel to show the rules.
     */
    public RulesPanel() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        JTextArea area = new JTextArea(30, 50);
        JScrollPane pane = new JScrollPane(area);
        area.setText(RULES);
        area.setEditable(false);
        area.setLineWrap(true);
        area.setWrapStyleWord(true);
        add(pane);
        JButton returnButton = new JButton("Back");
        returnButton.addActionListener(ignored -> Main.getFrame().setPanel(new StartPanel()));
        returnButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(returnButton);
    }
}
