package hk.ust.comp3111.ui;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DirectionTest {
    
    @Test
    void toDirection() {
        // target function: Direction.toDirection(String)
        assertEquals(Direction.UP, Direction.toDirection("W"));
        // target function: Direction.toDirection(String)
        assertEquals(Direction.LEFT, Direction.toDirection("A"));
        // target function: Direction.toDirection(String)
        assertEquals(Direction.DOWN, Direction.toDirection("S"));
        // target function: Direction.toDirection(String)
        assertEquals(Direction.RIGHT, Direction.toDirection("D"));
    }

    @Test
    void toNoneDirection() {
        // target function: Direction.toDirection(String)
        assertEquals(Direction.NONE, Direction.toDirection("C"));
    }
}