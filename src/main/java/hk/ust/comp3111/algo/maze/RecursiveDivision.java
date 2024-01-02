package hk.ust.comp3111.algo.maze;

import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;

import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import static hk.ust.comp3111.api.Vertex.TileType.*;

/**
 * Implements the recursive division algorithm for mazes.
 */
public final class RecursiveDivision implements MazeStrategy {
    private Consumer<MapData> cons = null;
    private final Random rand = new Random();
    @Override
    public void generate(MapData data) {
        data.forAll(v -> v.setTileType(CLEAR));
        recursiveSlice(data, 0, MapData.DEFAULT_WIDTH, 0, MapData.DEFAULT_HEIGHT);
        Vertex entrance = MazeHelper.generateAtCol(data, 0);
        Vertex exit = MazeHelper.generateAtCol(data, MapData.DEFAULT_WIDTH - 1);
        entrance.setTileType(ENTRANCE);
        exit.setTileType(EXIT);
        MazeHelper.addPath(data);
        if (cons != null) {
            cons.accept(data);
        }
    }

    /**
     * Recursively partitions the map data given into two partitions by adding columns and/or rows.
     * If the width is larger than the height, partition vertically.
     * If the height is larger than the width, partition horizontally.
     * If they are equal, partition randomly.<br>
     * This is an internal method. Do not call this unless you are a test. (And this should not be callable anyways, given that the class is final).
     * This is only protected for testing purposes. Otherwise, this should be an implementation detail.
     * @param data The map data to modify
     * @param widthLeft The left column to partition; inclusive
     * @param widthRight The right column to partition; exclusive
     * @param heightLo The upper row to partition; inclusive
     * @param heightHi The lower row to partition; exclusive
     */
    void recursiveSlice(MapData data, int widthLeft, int widthRight, int heightLo, int heightHi) {
        int widthDiff = widthRight - widthLeft;
        int heightDiff = heightHi - heightLo;
        if (widthDiff <= 2 || heightDiff <= 2) {
            return;
        }
        if (widthDiff > heightDiff) {
            // vertical slice
            verticalSlice(data, widthLeft, widthRight, heightLo, heightHi);
        } else if (widthDiff < heightDiff){
            horizontalSlice(data, widthLeft, widthRight, heightLo, heightHi);
        } else {
            if (rand.nextBoolean()) {
                verticalSlice(data, widthLeft, widthRight, heightLo, heightHi);
            } else {
                horizontalSlice(data, widthLeft, widthRight, heightLo, heightHi);
            }
        }
    }

    /**
     * Vertically partitions the given set by adding a column of barriers with a hole.<br>
     * This is an internal method. Do not call this unless you are a test. (And this should not be callable anyways, given that the class is final).
     * This is only protected for testing purposes. Otherwise, this should be an implementation detail.
     * @param data The map data to modify
     * @param widthLeft The left column to partition; inclusive
     * @param widthRight The right column to partition; exclusive
     * @param heightLo The upper row to partition; inclusive
     * @param heightHi The lower row to partition; exclusive
     */
    void verticalSlice(MapData data, int widthLeft, int widthRight, int heightLo, int heightHi) {
        int sliceCol, hole;
        do  {
            sliceCol = rand.nextInt(widthLeft, widthRight - 1);
            hole = rand.nextInt(heightLo, heightHi);

        } while (sliceCol % 2 != 0 || hole % 2 == 0);
        int finalCol = sliceCol;
        int finalHole = hole;
        IntStream.range(heightLo, heightHi)
                .mapToObj(i -> data.at(new Coord(finalCol, i)))
                .flatMap(Optional::stream)
                .forEach(v -> v.setTileType(v.y() == finalHole ? CLEAR : BARRIER));
        recursiveSlice(data, widthLeft, sliceCol, heightLo, heightHi);
        recursiveSlice(data, sliceCol + 1, widthRight, heightLo, heightHi);
    }

    /**
     * Horizontally partitions the given set by adding a row of barriers with one hole.<br>
     * This is an internal method. Do not call this unless you are a test. (And this should not be callable anyways, given that the class is final).
     * This is only protected for testing purposes. Otherwise, this should be an implementation detail.
     * @param data The map data to modify
     * @param widthLeft The left column to partition; inclusive
     * @param widthRight The right column to partition; exclusive
     * @param heightLo The upper row to partition; inclusive
     * @param heightHi The lower row to partition; exclusive
     */
    void horizontalSlice(MapData data, int widthLeft, int widthRight, int heightLo, int heightHi) {
        int sliceRow, hole;
        do {
            sliceRow = rand.nextInt(heightLo, heightHi - 1);
            hole = rand.nextInt(widthLeft, widthRight);
        } while (sliceRow % 2 != 0 || hole % 2 == 0);
        int finalRow = sliceRow;
        int finalHole = hole;
        IntStream.range(widthLeft, widthRight)
                .mapToObj(i -> data.at(new Coord(i, finalRow)))
                .flatMap(Optional::stream)
                .forEach(v -> v.setTileType(v.x() == finalHole ? CLEAR : BARRIER));
        recursiveSlice(data, widthLeft, widthRight, heightLo, sliceRow);
        recursiveSlice(data, widthLeft, widthRight, sliceRow + 1, heightHi);
    }
    @Override
    public void onUpdate(Consumer<MapData> toRun) {
        cons = toRun;
    }
}