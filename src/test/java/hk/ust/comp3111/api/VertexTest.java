package hk.ust.comp3111.api;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.awt.*;
import java.util.stream.IntStream;

import static hk.ust.comp3111.api.Vertex.TileType.*;
import static org.junit.jupiter.api.Assertions.*;

class VertexTest {
    Vertex v1;
    Vertex v2;
    Vertex v3;
    Vertex v4;
    @BeforeEach
    void setUp() {
        v1 = new Vertex(new Coord(2, 3), BARRIER);
        v2 = new Vertex(new Coord(17, 0), ENTRANCE);
        v3 = new Vertex(new Coord(17, 28), CLEAR);
        v4 = new Vertex(new Coord(17, 28), CLEAR);
    }

    @Test
    void getCoord() {
        // target function: Vertex.getCoord()
        assertEquals(new Coord(2, 3), v1.getCoord());
        // target function: Vertex.getCoord()
        assertEquals(new Coord(17, 0), v2.getCoord());
        // target function: Vertex.getCoord()
        assertEquals(new Coord(17, 28), v3.getCoord());
        // target function: Vertex.getCoord()
        assertNotEquals(new Coord(1021, 2011), v1.getCoord());
    }

    @Test
    void x() {
        // target function: Vertex.x()
        assertEquals(2, v1.x());
        // target function: Vertex.x()
        assertEquals(17, v2.x());
        // target function: Vertex.x()
        assertEquals(17, v3.x());
        // target function: Vertex.x()
        assertNotEquals(2611, v1.x());
    }

    @Test
    void y() {
        // target function: Vertex.y()
        assertEquals(3, v1.y());
        // target function: Vertex.y()
        assertEquals(0, v2.y());
        // target function: Vertex.y()
        assertEquals(28, v3.y());
        // target function: Vertex.y()
        assertNotEquals(3721, v1.y());
    }

    @Test
    void getTileType() {
        // target function: Vertex.getTileType()
        assertEquals(BARRIER, v1.getTileType());
        // target function: Vertex.getTileType()
        assertEquals(ENTRANCE, v2.getTileType());
        // target function: Vertex.getTileType()
        assertEquals(CLEAR, v3.getTileType());
        // target function: Vertex.getTileType()
        assertNotEquals(EXIT, v3.getTileType());
    }

    @Test
    void setTileType() {
        // target function: Vertex.setTileType(TileType)
        v1.setTileType(BARRIER);
        // target function: Vertex.setTileType(TileType)
        v2.setTileType(EXIT);
        assertEquals(BARRIER, v1.getTileType());
        assertEquals(EXIT, v2.getTileType());
    }

    @Test
    void compareTo() {
        // target function: Vertex.compareTo(Vertex)
        assertTrue(v1.compareTo(v2) > 0);
        // target function: Vertex.compareTo(Vertex)
        assertTrue(v2.compareTo(v3) < 0);
        // target function: Vertex.compareTo(Vertex)
        assertEquals(0, v3.compareTo(v4));
    }

    @Test
    void testValues() {
        // Target function: new TileType(Color)
        Vertex.TileType[] tileTypes = Vertex.TileType.values();
        IntStream.range(0, tileTypes.length).forEach(i -> assertEquals(i, tileTypes[i].ordinal()));
    }

    @Test
    void from() {
        // target function: Vertex.TileType.from(int)
        assertEquals(CLEAR, Vertex.TileType.from(0));
        // target function: Vertex.TileType.from(int)
        assertEquals(BARRIER, Vertex.TileType.from(1));
        // target function: Vertex.TileType.from(int)
        assertEquals(ENTRANCE, Vertex.TileType.from(2));
        // target function: Vertex.TileType.from(int)
        assertEquals(EXIT, Vertex.TileType.from(3));
    }
    @Test
    void fromException() {
        // target function: Vertex.TileType.from(int)
        assertThrows(IllegalArgumentException.class, () -> Vertex.TileType.from(-1));
        // target function: Vertex.TileType.from(int)
        assertThrows(IllegalArgumentException.class, () -> Vertex.TileType.from(10));
    }

    @Test
    void color() {
        // target function: Vertex.TileType.getColor()
        assertEquals(Color.DARK_GRAY, v1.getTileType().getColor());
        // target function: Vertex.TileType.getColor()
        assertEquals(Color.GREEN, v2.getTileType().getColor());
        // target function: Vertex.TileType.getColor()
        assertEquals(Color.WHITE, v3.getTileType().getColor());
        // target function: Vertex.TileType.getColor()
        assertEquals(Color.WHITE, v4.getTileType().getColor());
    }

    @Test
    void testEquals() {
        Vertex v5 = new Vertex(new Coord(2, 3), BARRIER);
        // target function: Vertex.equals(Object)
        assertTrue(v1.equals(v1));
        // target function: Vertex.equals(Object)
        assertFalse(v1.equals(new Coord(0, 0)));
        // target function: Vertex.equals(Object)
        assertTrue(v1.equals(v5));
    }

    @Test
    void testHashCode() {
        // target function: Vertex.hashCode()
        assertEquals(v3.hashCode(), v4.hashCode());
        // target function: Vertex.hashCode()
        assertNotEquals(v1.hashCode(), v2.hashCode());
    }

    @Test
    void toStringTest() {
        // target function: Vertex.toString()
        assertEquals("(2, 3), BARRIER", v1.toString());
        // target function: Vertex.toString()
        assertNotEquals("(2, 3), BARRIER", v2.toString());
    }

    @Test
    void newVertex() {
        // target function: new Vertex(Coord, TileType)
        assertEquals(BARRIER, new Vertex(new Coord(0, 7), BARRIER).getTileType());
        // target function: new Vertex(Coord, TileType)
        assertEquals(new Coord(0, 7), new Vertex(new Coord(0, 7), BARRIER).getCoord());
    }
}