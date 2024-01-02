package hk.ust.comp3111.api;

import hk.ust.comp3111.engine.Entity;
import hk.ust.comp3111.engine.ImagePath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static hk.ust.comp3111.api.Vertex.TileType.*;
import static org.junit.jupiter.api.Assertions.*;

class MapDataTest {
    private final String expectedMap = """
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
2, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 3,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0,
1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1,
0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0""";
    MapData data;
    private List<List<Vertex>> vert;
    final Map<ImagePath, Path> imagePaths = ImagePath.IMAGES;

    Vertex v1;
    Vertex v2;
    Vertex v3;
    final Entity e1 = new Entity(imagePaths, ImagePath.CRYSTAL) {};
    final Entity e2 = new Entity(imagePaths, ImagePath.CRYSTAL) {};

    @BeforeEach
    void setUp() {
        vert = new ArrayList<>();
        for (int y = 0; y < MapData.DEFAULT_HEIGHT; y++) {
            List<Vertex> currentRow = new ArrayList<>();
            for (int x = 0; x < MapData.DEFAULT_WIDTH; x++) {
                if (y == 13 && x == 0) {
                    currentRow.add(new Vertex(new Coord(x, y), ENTRANCE));
                } else if (y == 17 && x == 29) {
                    currentRow.add(new Vertex(new Coord(x, y), EXIT));
                } else if (y % 2 == 0) {
                    currentRow.add(new Vertex(new Coord(x, y), BARRIER));
                } else {
                    currentRow.add(new Vertex(new Coord(x, y), CLEAR));
                }
            }
            vert.add(currentRow);
        }
        data = new MapData(vert);

        v1 = new Vertex(new Coord(2, 3), BARRIER);
        v2 = new Vertex(new Coord(17, 0), ENTRANCE);
        v3 = new Vertex(new Coord(17, 28), CLEAR);
    }

    @Test
    void at() {
        // target function: MapData.at(Coord)
        assertEquals(ENTRANCE, data.at(new Coord(0, 13)).orElseThrow().getTileType());
        // target function: MapData.at(Coord)
        assertEquals(EXIT, data.at(new Coord(29, 17)).orElseThrow().getTileType());
        // target function: MapData.at(Coord)
        assertEquals(BARRIER, data.at(new Coord(28, 28)).orElseThrow().getTileType());
        // target function: MapData.at(Coord)
        assertEquals(CLEAR, data.at(new Coord(16, 3)).orElseThrow().getTileType());
    }
    @Test
    void atOutOfBounds() {
        // target function: MapData.at(Coord)
        assertTrue(data.at(new Coord(30624700, 30624700)).isEmpty());
        // target function: MapData.at(Coord)
        assertTrue(data.at(new Coord(-3511, -2012)).isEmpty());
    }

    /*
    Note for TAs grading: This is NOT a unit test.
    This is a test for performance which is used for monitoring performance impacts.
     */
    @Test
    @Timeout(value = 250, unit = TimeUnit.MILLISECONDS)
    void atPerformance() {
        // 900 ** 3 = 729,000,000 = 24,300,000 * 30
        // 900 ** 2 = 810,000 = 27,000 * 30
        for (int i = 0; i < 27000; i++) {
            for (int j = 0; j < 30; j++) {
                data.at(new Coord(2, j));
            }
        }
    }

    @Test
    void getVertices() {
        // target function: MapData.getVertices()
        assertEquals(vert, data.getVertices());
        // target function: MapData.getVertices()
        assertEquals(MapData.DEFAULT_WIDTH, data.getVertices().size());
        // target function: MapData.getVertices()
        assertEquals(MapData.DEFAULT_HEIGHT, data.getVertices().get(0).size());
    }

    @Test
    void set() {
        // target function: MapData.set(Vertex)
        data.set(new Vertex(new Coord(23, 2), ENTRANCE));
        assertEquals(ENTRANCE, data.at(new Coord(23, 2)).orElseThrow().getTileType());
    }

    @Test
    void toCsv() {
        // target function: MapData.toCsv(List<Vertex>)
        assertEquals(expectedMap, MapData.toCsv(vert));
    }

    @Test
    void addEntity() {
        // target function: MapData.addEntity(Vertex, Entity)
        assertThrows(IllegalStateException.class, () -> data.addEntity(v1, e1));
        Set<Entity> entities = data.getEntities();
        // target function: MapData.addEntity(Vertex, Entity)
        data.addEntity(v2, e1);
        assertTrue(entities.contains(e1));
        assertFalse(entities.contains(e2));
    }

    @Test
    void removeEntity() {
        Set<Entity> entities = data.getEntities();
        data.addEntity(v2, e1);
        // target function: MapData.removeEntity(Entity)
        data.removeEntity(e1);
        assertFalse(entities.contains(e1));
    }

    @Test
    void hasEntity() {
        Set<Entity> entities = data.getEntities();
        data.addEntity(v2, e1);
        // target function: MapData.hasEntity(Entity)
        assertTrue(data.hasEntity(e1));
        data.removeEntity(e1);
        // target function: MapData.hasEntity(Entity)
        assertFalse(data.hasEntity(e1));
    }

    @Test
    void entityLocation() {
        data.addEntity(v2, e1);
        // target function: MapData.entityLocation(Entity)
        assertEquals(v2, data.entityLocation(e1));
    }

    @Test
    void isPassable() {
        // target function: MapData.isPassable(Vertex)
        assertFalse(data.isPassable(v1));
        // target function: MapData.isPassable(Vertex)
        assertTrue(data.isPassable(v2));
        // target function: MapData.isPassable(Vertex)
        assertTrue(data.isPassable(v3));
    }

    @Test
    void write() throws IOException {
        Files.createDirectories(Path.of("test-output"));
        Path path = Path.of("test-output" + File.separator + "test.csv");
        // target function: MapData.write(Path)
        data.write(path);
        MapData read = new MapData(path);
        assertEquals(read, data);
    }

    @Test
    void testEquals() {
        MapData data2 = data;
        // target function: MapData.equals(Object)
        assertTrue(data.equals(data2));
        // target function: MapData.equals(Object)
        assertFalse(data.equals(v1));

        data2 = new MapData(vert);
        // target function: MapData.equals(Object)
        assertTrue(data.equals(data2));
    }

    @Test
    void testHashCode() throws IOException {
        Files.createDirectories(Path.of("test-output"));
        Path path = Path.of("test-output"  + File.separator +"test.csv");
        data.write(path);
        MapData read = new MapData(path);
        // target function: MapData.hashCode()
        assertEquals(read.hashCode(), data.hashCode());
    }

    @Test
    void forAll() {
        // target function: MapData.forAll(Consumer<Vertex>)
        data.forAll(v -> v.setTileType(BARRIER));
        assertEquals(BARRIER, data.at(new Coord(0, 0)).orElseThrow().getTileType());
        assertNotEquals(ENTRANCE, data.at(new Coord(17, 12)).orElseThrow().getTileType());
        // target function: MapData.forAll(Consumer<Vertex>)
        data.forAll(v -> v.setTileType(CLEAR));
        assertEquals(CLEAR, data.at(new Coord(3, 4)).orElseThrow().getTileType());
    }

    @Test
    void lerpVertical() {
        List<Vertex> verticalVertices = IntStream.rangeClosed(2, 8).mapToObj(i -> data.at(new Coord(0, i)))
                .flatMap(Optional::stream)
                .toList();
        Vertex verticalDown = data.at(new Coord(0, 2)).orElseThrow();
        Vertex verticalUp = data.at(new Coord(0, 8)).orElseThrow();
        // target function: MapData.lerp(Vertex, Vertex)
        assertEquals(verticalVertices, data.lerp(verticalUp, verticalDown));
        // target function: MapData.lerp(Vertex, Vertex)
        assertEquals(verticalVertices, data.lerp(verticalDown, verticalUp));
        // target function: MapData.lerp(Vertex, Vertex)
        assertEquals(List.of(verticalUp), data.lerp(verticalUp, verticalUp));
    }
    @Test
    void lerpHorizontal() {
        List<Vertex> horizontalVertices = IntStream.rangeClosed(2, 8).mapToObj(i -> data.at(new Coord(i, 1)))
                .flatMap(Optional::stream)
                .toList();
        Vertex horizontalRight = data.at(new Coord(2, 1)).orElseThrow();
        Vertex horizontalLeft = data.at(new Coord(8, 1)).orElseThrow();
        // target function: MapData.lerp(Vertex, Vertex)
        assertEquals(horizontalVertices, data.lerp(horizontalRight, horizontalLeft));
        // target function: MapData.lerp(Vertex, Vertex)
        assertEquals(horizontalVertices, data.lerp(horizontalLeft, horizontalRight));
    }
    @Test
    void lerpSlanted() {
        Vertex downLeft = data.at(new Coord(4, 3)).orElseThrow();
        Vertex upRight = data.at(new Coord(6, 4)).orElseThrow();
        List<Vertex> slantedUpExpected = List.of(data.at(new Coord(4, 3)).orElseThrow(),
                data.at(new Coord(5, 4)).orElseThrow(), data.at(new Coord(6, 4)).orElseThrow());
        // target function: MapData.lerp(Vertex, Vertex)
        assertEquals(slantedUpExpected, data.lerp(downLeft, upRight));

        Vertex upLeft = downLeft;
        Vertex downRight = data.at(new Coord(6, 2)).orElseThrow();
        List<Vertex> slantedDownExpected = List.of(data.at(new Coord(4,3)).orElseThrow(),
                data.at(new Coord(5,2)).orElseThrow(), data.at(new Coord(6,2)).orElseThrow());
        // target function: MapData.lerp(Vertex, Vertex)
        assertEquals(slantedDownExpected, data.lerp(upLeft, downRight));
    }

    @Test
    void lerpRightUp() {
        Vertex downLeft = data.at(new Coord(0, 0)).orElseThrow();
        Vertex upRight = data.at(new Coord(29, 3)).orElseThrow();
        // target function: MapData.lerp(Vertex, Vertex)
        assertEquals(IntStream.range(0, 30)
                        .mapToObj(i -> data.at(new Coord(i, (i + 5) / 10)))
                                .flatMap(Optional::stream)
                                        .collect(Collectors.toList()),
        data.lerp(downLeft, upRight));
    }

    @Test
    void findEntrance() {
        // target function: MapData.findEntrance()
        Optional<Vertex> optEntrance = data.findEntrance();
        assertTrue(optEntrance.isPresent());
        optEntrance.orElseThrow().setTileType(BARRIER);
        // target function: MapData.findEntrance()
        assertFalse(data.findEntrance().isPresent());
    }

    @Test
    void findExit() {
        // target function: MapData.findExit()
        Optional<Vertex> optExit = data.findExit();
        assertTrue(optExit.isPresent());
        optExit.orElseThrow().setTileType(BARRIER);
        // target function: MapData.findExit()
        assertFalse(data.findExit().isPresent());
    }

    @Test
    void around() {
        // target function: MapData.around(Vertex, int)
        assertEquals(2, data.around(data.at(new Coord(0, 0)).orElseThrow(), 2).size());
        // target function: MapData.around(Vertex, int)
        assertEquals(3, data.around(data.at(new Coord(12, 0)).orElseThrow(), 1).size());
        // target function: MapData.around(Vertex, int)
        assertEquals(4, data.around(data.at(new Coord(22, 17)).orElseThrow(), 1).size());
    }

    @Test
    void getEntities() {
        data.addEntity(v2, e1);
        data.addEntity(v2, e2);
        // target function: MapData.getEntities()
        assertEquals(2, data.getEntities().size());
        data.removeEntity(e1);
        // target function: MapData.getEntities()
        assertEquals(1, data.getEntities().size());
        // target function: MapData.getEntities()
        assertTrue(data.getEntities().contains(e2));
    }

    @Test
    void newMapDataVertices() {
        List<List<Vertex>> vs = MapDataHelper.getMap().getVertices();
        // target function: new MapData(List<List<Vertex>>)
        MapData d1 = new MapData(vs);
        assertEquals(vs, d1.getVertices());
    }

    @Test
    void newMapDataPath() throws IOException {
        // target function: new MapData(Path)
        assertEquals(ENTRANCE,
                new MapData(Path.of("test-inputs" + File.separator + "maze-one.csv"))
                        .at(new Coord(0, 29)).orElseThrow().getTileType());
    }
}