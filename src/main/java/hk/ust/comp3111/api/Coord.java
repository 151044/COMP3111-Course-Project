package hk.ust.comp3111.api;

/**
 * Represents a coordinate in the Cartesian coordinate plane.
 * @param x The x-coordinate. Represents the column.
 * @param y The y-coordinate. Represents the row.
 */
public record Coord (int x, int y) {
    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    /**
     * Adds this coordinate and the other coordinate together.
     * @return A new coordinate (this.x + other.x, this.y + other.y)
     */
    public Coord translate(Coord other) {
        return new Coord(this.x + other.x, this.y + other.y);
    }
}
