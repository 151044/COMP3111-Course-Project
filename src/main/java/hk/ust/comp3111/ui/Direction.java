package hk.ust.comp3111.ui;

/**
 * The enum representing movement directions in the game.
 */
public enum Direction {
    /**
     * The up direction.
     */
    UP,
    /**
     * The down direction.
     */
    DOWN,
    /**
     * The left direction.
     */
    LEFT,
    /**
     * The right direction.
     */
    RIGHT,
    /**
     * The none direction.
     */
    NONE;

    /**
     * Translates a String ("W", "A", "S", "D") to a Direction.
     * @param string The String to use
     * @return The corresponding direction; or NONE if the key event does not correspond to any known input.
     */
    public static Direction toDirection(String string) {
        return switch (string) {
            case "W" -> UP;
            case "A" -> LEFT;
            case "S" -> DOWN;
            case "D" -> RIGHT;
            default -> NONE;
        };
    }
}
