package hk.ust.comp3111.algo.maze;

import hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch;
import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;
import hk.ust.comp3111.reflect.Reflect;
import hk.ust.comp3111.api.MapDataHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RecursiveDivisionTest {

    private RecursiveDivision div;
    private BreadthFirstSearch search;
    private Random random;

    @BeforeEach
    void hacks() throws NoSuchFieldException, IllegalAccessException {
        random = new Random(69420);
        div = new RecursiveDivision();
        Reflect.setField("rand", div, random);
        search = new BreadthFirstSearch();
    }

    @Test
    void generate() {
        MapData data = MapDataHelper.getMap();
        AtomicBoolean bool = new AtomicBoolean();
        div.onUpdate(d -> bool.set(true));
        // target function: RecursiveDivision.generate(MapData)
        div.generate(data);
        assertFalse(data.getVertices().stream().flatMap(List::stream)
                .allMatch(v -> v.getTileType() == Vertex.TileType.BARRIER));
        assertTrue(data.findEntrance().isPresent());
        assertTrue(data.findEntrance().isPresent());
        assertTrue(search.bfsGeneral(data,
                data.findEntrance().orElseThrow(),
                data.findExit().orElseThrow(), true).size() > 0);
    }

    @Test
    void onUpdate() {
        MapData data = MapDataHelper.getMap();
        AtomicBoolean bool = new AtomicBoolean();
        // target function: RecursiveDivision.onUpdate(Consumer<MapData>)
        div.onUpdate(d -> bool.set(true));
        div.generate(data);
        assertTrue(bool.get());
    }

    @Test
    void recursiveSlice() throws NoSuchFieldException, IllegalAccessException {
        RecursiveDivision mockedDiv = Mockito.mock(RecursiveDivision.class);
        doNothing().when(mockedDiv).horizontalSlice(any(), anyInt(), anyInt(), anyInt(), anyInt());
        doNothing().when(mockedDiv).verticalSlice(any(), anyInt(), anyInt(), anyInt(), anyInt());
        doCallRealMethod().when(mockedDiv).recursiveSlice(any(), anyInt(), anyInt(), anyInt(), anyInt());
        random = Mockito.spy(random);
        Reflect.setField("rand", mockedDiv, random);
        Consumer<MapData> cons = Objects::requireNonNull;
        doCallRealMethod().when(mockedDiv).onUpdate(cons);
        mockedDiv.onUpdate(cons);

        MapData data = MapDataHelper.getMap();
        // target function: RecursiveDivision.recursiveSlice(MapData, int, int, int, int)
        mockedDiv.recursiveSlice(data, 0, MapData.DEFAULT_WIDTH, 0, MapData.DEFAULT_HEIGHT);
        when(random.nextBoolean()).thenReturn(false);
        // target function: RecursiveDivision.recursiveSlice(MapData, int, int, int, int)
        mockedDiv.recursiveSlice(data, 0, MapData.DEFAULT_WIDTH, 0, MapData.DEFAULT_HEIGHT);
        // target function: RecursiveDivision.recursiveSlice(MapData, int, int, int, int)
        mockedDiv.recursiveSlice(data, 0, MapData.DEFAULT_WIDTH, 0, 12);
        // target function: RecursiveDivision.recursiveSlice(MapData, int, int, int, int)
        mockedDiv.recursiveSlice(data, 0, 18, 0, MapData.DEFAULT_HEIGHT);
    }

    @Test
    void horizontalSlice() throws NoSuchFieldException, IllegalAccessException{
        RecursiveDivision mockedDiv = Mockito.mock(RecursiveDivision.class);
        random = Mockito.spy(random);
        Reflect.setField("rand", mockedDiv, random);
        when(random.nextInt(0, MapData.DEFAULT_WIDTH - 1)).thenReturn(18);
        doNothing().when(mockedDiv).recursiveSlice(any(), anyInt(), anyInt(), anyInt(), anyInt());
        doCallRealMethod().when(mockedDiv).horizontalSlice(any(), anyInt(), anyInt(), anyInt(), anyInt());
        MapData data = MapDataHelper.getMap();
        // target function: RecursiveDivision.horizontalSlice(MapData, int, int, int, int)
        mockedDiv.horizontalSlice(data, 0, MapData.DEFAULT_WIDTH, 0, MapData.DEFAULT_HEIGHT);
        assertEquals(29, IntStream.range(0, 30).mapToObj(i -> data.at(new Coord(i, 18)))
                .flatMap(Optional::stream)
                .filter(v -> v.getTileType().equals(Vertex.TileType.BARRIER))
                .count());
        assertEquals(1, IntStream.range(0, 30).mapToObj(i -> data.at(new Coord(i, 18)))
                .flatMap(Optional::stream)
                .filter(v -> v.getTileType().equals(Vertex.TileType.CLEAR))
                .count());
    }

    @Test
    void verticalSlice() throws NoSuchFieldException, IllegalAccessException {
        RecursiveDivision mockedDiv = Mockito.mock(RecursiveDivision.class);
        random = Mockito.spy(random);
        Reflect.setField("rand", mockedDiv, random);
        when(random.nextInt(0, MapData.DEFAULT_WIDTH - 1)).thenReturn(18);
        doNothing().when(mockedDiv).recursiveSlice(any(), anyInt(), anyInt(), anyInt(), anyInt());
        doCallRealMethod().when(mockedDiv).verticalSlice(any(), anyInt(), anyInt(), anyInt(), anyInt());
        MapData data = MapDataHelper.getMap();
        mockedDiv.verticalSlice(data, 0, MapData.DEFAULT_WIDTH, 0, MapData.DEFAULT_HEIGHT);
        // target function: RecursiveDivision.verticalSlice(MapData, int, int, int, int)
        assertEquals(29, IntStream.range(0, 30).mapToObj(i -> data.at(new Coord(18, i)))
                .flatMap(Optional::stream)
                .filter(v -> v.getTileType().equals(Vertex.TileType.BARRIER))
                .count());
        assertEquals(1, IntStream.range(0, 30).mapToObj(i -> data.at(new Coord(18, i)))
                .flatMap(Optional::stream)
                .filter(v -> v.getTileType().equals(Vertex.TileType.CLEAR))
                .count());
    }
}