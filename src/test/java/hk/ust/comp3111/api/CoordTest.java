package hk.ust.comp3111.api;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CoordTest {
    final Coord c1 = new Coord(1000, 2000);
    final Coord c2 = new Coord(1, 2);
    final Coord c3 = new Coord(3, -3632);

    @Test
    void testToString() {
        // target function: Coord.toString()
        assertEquals("(1000, 2000)", c1.toString());
        // target function: Coord.toString()
        assertEquals("(1, 2)", c2.toString());
        // target function: Coord.toString()
        assertEquals("(3, -3632)", c3.toString());
    }

    @Test
    void x() {
        // target function: Coord.x()
        assertEquals(1000, c1.x());
        // target function: Coord.x()
        assertEquals(1, c2.x());
        // target function: Coord.x()
        assertEquals(3, c3.x());
    }

    @Test
    void y() {
        // target function: Coord.y()
        assertEquals(2000, c1.y());
        // target function: Coord.y()
        assertEquals(2, c2.y());
        // target function: Coord.y()
        assertEquals(-3632, c3.y());
    }

    @Test
    void translate() {
        // target function: Coord.translate(Coord)
        assertEquals(new Coord(1001, 2002), c1.translate(c2));
        // target function: Coord.translate(Coord)
        assertEquals(new Coord(4, -3630), c2.translate(c3));
        // target function: Coord.translate(Coord)
        assertEquals(new Coord(1003, -1632), c1.translate(c3));
    }

    @Test
    void newCoord() {
        // target function: new Coord(int, int)
        assertEquals(7, new Coord(7, 0).x());
        // target function: new Coord(int, int)
        assertEquals(7, new Coord(0, 7).y());
    }
}