# COMP 3111(H) Fall 2023 Course Project
![Actions_Badge](https://github.com/151044/COMP3111-Course-Project/actions/workflows/gradle.yml/badge.svg)

This project was done for COMP 3111(H) of HKUST in Fall 2023. This project had 3 collaborators.
- [@151044](https://github.com/151044) (Owner)
- [@anormalperson8](https://github.com/anormalperson8)
- [@Phantamysteria](https://github.com/Phantasmysteria)

This repository was cloned to avoid revealing sensitive personal information, and hence lack detailed commit history. <br/>
Contact any one of us for details if necessary.

In this course, which is one of the courses of all time, we finish a project which is one of the projects of all time.

## Prerequisites
This application requires Java 17 or later. There has been notable GUI slowdowns with certain versions of JDK 17 on Windows - please grab the [Amazon Correto JDK](https://docs.aws.amazon.com/corretto/latest/corretto-17-ug/downloads-list.html) if the UI is very sluggish.

## User Manual
There are three buttons in the initial UI, each corresponding to one function:
### Function A: Maze Generator (Edit Map)
There are three rows of buttons at the bottom which can be used to perform a variety of actions.
The following modes exist for the manual maze editor (first button row):
- Toggle: Clicking on a square which is not empty will turn it into a barrier, and vice versa.
- Clear: Clears the entire grid, creating a grid with only clear squares
- Fill: Fills the entire grid, creating a grid with only barrier squares
- Line: First toggles the clicked square as per the behaviour in Toggle, then dragging the mouse will cause a line of said tile to be drawn. Supports dynamic rendering - lines are not confirmed until the mouse is released.
- Entrance: Places the entrance tile. The tile must be on the edge of the maze.
- Exit: Places the exit tile. The tile must be on the edge of the maze.

Placing an entrance/exit tile will automatically remove the previous entrance or exit.

By default, the mode of the maze generator is "Toggle".
Entrance tiles are green, while exit tiles are yellow.

The second row of buttons are maze generating algorithms:
- Recursive Division: Generates mazes by recursively dividing the entire grid by placing walls. Can generate mazes with 2-tall corridors.
- Prim's Algorithm: Generates mazes by using a randomized version of Prim's algorithm. Tends to make mazes with shorter dead-ends.
- Recursive Backtracking: Generates mazes by randomly walking along the grid, then backtracking when it is no longer possible to walk in a random direction. Generates mazes with longer loops.

The third row are buttons for loading a map, saving the current map, and returning to menu without saving.
Our version of the CSV file exported uses "2" to represent the entrance, and "3" to represent the exit.
### Function B: Shortest Path (Shortest Path)
The application first prompts the user to select a CSV file, then loads the given file as a map.

It then runs the shortest path algorithm and highlights the path in red, as well as displaying the vertices passed in a separate table.

The format of the CSV file produced by the "Save as CSV" button is, per row, "row, col, ", without the quotation marks.
### Function C: Play Game (Start Game)
The application first prompts the user to select a CSV file as a map, then starts the game immediately.

You can control Jerry by either using WASD keys, or arrow keys as you prefer. Your goal is to guide Jerry to the exit without being devoured by Tom.

The application returns to the main menu after you win or lose.

You can see a detailed list of rules in the "Rules" page from the starting panel.

Note: Default maps are stored in the `default-maps` subdirectory of this project.

### Themes
Our UI uses the modern [FlatLaf](https://www.formdev.com/flatlaf/) library. You can choose between different themes provided by the library in the Settings panel.
## Code Execution
Our build system is Gradle.
### Running the Application
**If you are using IntelliJ**:

Run the `Main` configuration.

If you wish to generate a runnable JAR (as below), run the `GradleJar` configuration.

**If you do not want to use an IDE**, create a runnable JAR as follows:

For Mac/Unix:
```
./gradlew dist
```

For Windows:
```
.\gradlew.bat dist
```

The runnable application JAR can be found under `build/libs`.

If double-clicking on the JAR does not bring up anything, your Java environment may be misconfigured. Try to run it from the command line in the `build/libs` directory:
```
java -jar COMP3111-Project-1.0.jar
```

### Running the Tests
We have elected to use JUnit 5 as our testing framework.

**If you are using IntelliJ**:

Run the `AllTests` configuration.

If you wish to use Gradle to run all tests, run the `GradleTest` configuration.

**If you do not want to use an IDE**, then you can run the tests as follows:

For Mac/Unix:
```
./gradlew test
```

For Windows:
```
.\gradlew.bat test
```

**Note: If the tests are run in a headless environment (on a server without a GUI, for example), tests related to UI will be skipped. You can check if tests are skipped by looking for the word SKIPPED in the gradle output.**
