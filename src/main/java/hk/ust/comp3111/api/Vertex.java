package hk.ust.comp3111.api;

import java.awt.*;
import java.util.Comparator;
import java.util.Objects;

/**
 * A vertex in the game. Contains all the information about a single square on the game map.
 */
public class Vertex implements Comparable<Vertex> {
    private final Coord coord;
    private TileType tileType;
    private static final Comparator<Vertex> comparator = Comparator.comparingInt(Vertex::y)
            .thenComparing(Vertex::x);

    /**
     * Constructs a new {@link Vertex} with the specified {@link TileType} at the coordinates given.
     * @param c The coordinates of this vertex
     * @param type The type of tile
     */
    public Vertex(Coord c, TileType type) {
        this.coord = c;
        this.tileType = type;
    }

    /**
     * Gets the coordinates of this vertex.
     * @return The coordinates of this vertex
     */

    public Coord getCoord() {
        return coord;
    }

    /**
     * Convenience method to get the x-coordinate of this vertex.
     * @return The x-coordinate of this vertex
     */
    public int x() {
        return coord.x();
    }

    /**
     * Convenience method to get the y-coordinate of this vertex.
     * @return The y-coordinate of this vertex
     */
    public int y() {
        return coord.y();
    }

    /**
     * Gets the {@link TileType} of this vertex.
     * @return The type of tile that this vertex is
     */

    public TileType getTileType() {
        return tileType;
    }

    /**
     * Sets the tile type of this vertex.
     * @param type The type to set
     */
    public void setTileType(TileType type) {
        this.tileType = type;
    }

    @Override
    public int compareTo(Vertex vertex) {
        return comparator.compare(this, vertex);
    }

    /**
     * An enum representing the type of the vertex.
     * Use {@link Enum#ordinal()} to obtain the value for writing to .csv.
     */
    public enum TileType {
        /**
         * A clear vertex. Passable by entities.
         */
        CLEAR(Color.WHITE),
        /**
         * A barrier. Cannot be passed.
         */
        BARRIER(Color.DARK_GRAY),
        /**
         * The entrance tile. Passable by entities.
         */
        ENTRANCE(Color.GREEN),
        /**
         * The exit tile. Passable by entities.
         */
        EXIT(Color.ORANGE);

        /**
         * Gets the color of this vertex.
         * @return The color to render on the screen
         */
        public Color getColor() {
            return c;
        }
        private final Color c;
        // private: no doc required?
        TileType(Color c) {
            this.c = c;
        }

        /**
         * Gets a {@link TileType TileType} from the ordinal returned by {@link Enum#ordinal()}.
         * @param ordinal The index of the {@link TileType TileType}, in declaration order
         * @return The corresponding {@link TileType TileType}
         * @throws IllegalArgumentException If the index provided is out of range.
         */
        public static TileType from(int ordinal) {
            // Note: This is **bad practice** in normal programming.
            // We should not be using ordinal() directly, but I am lazy.
            return switch (ordinal) {
                case 0 -> CLEAR;
                case 1 -> BARRIER;
                case 2 -> ENTRANCE;
                case 3 -> EXIT;
                default -> throw new IllegalArgumentException("No such enum ordinal in TileType: " + ordinal);
            };
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex vertex)) return false;
        return Objects.equals(coord, vertex.coord) && tileType == vertex.tileType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(coord, tileType);
    }

    @Override
    public String toString() {
        return coord.toString() + ", " + tileType;
    }
}
