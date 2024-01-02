package hk.ust.comp3111.api;

import hk.ust.comp3111.api.Vertex.TileType;
import hk.ust.comp3111.engine.Entity;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * An object which contains the information of a game map.
 */
public class MapData {
    /**
     * The width of the map.
     */
    public static final int DEFAULT_WIDTH = 30;

    /**
     * The height of the map.
     */
    public static final int DEFAULT_HEIGHT = 30;

    // (row, column) = (y, x) = get(y).get(x)
    private List<List<Vertex>> vertices = new ArrayList<>();

    private final Map<Entity, Vertex> entities = new HashMap<>();

    /**
     * Constructs a new MapData object from the given list of vertices.
     * The input list is a list of vertices, ordered by rows.
     * Please use Coord(Height, Width) for the vertices passed.
     * @param other The list of vertices to copy
     */
    public MapData(List<List<Vertex>> other) {
        vertices = new ArrayList<>(other);
    }

    /**
     * Constructs a MapData object by reading from the given file.
     * @param input The path to read from
     * @throws IOException If the file cannot be read
     */
    public MapData(Path input) throws IOException {
        List<List<Vertex.TileType>> types = Files.readAllLines(input).stream()
                .map(s -> s.endsWith(",") ? s.substring(0, s.length() - 1) : s)
                .map(s -> List.of(s.split(",")))
                .map(s -> s.stream().map(String::trim)
                        .map(Integer::parseInt)
                        .map(Vertex.TileType::from).collect(Collectors.toList()))
                .toList();
        for (int i = 0; i < DEFAULT_HEIGHT; i++) {
            List<Vertex> vertex = new ArrayList<>();
            for (int j = 0; j < DEFAULT_WIDTH; j++) {
                vertex.add(new Vertex(new Coord(j, i), types.get(i).get(j)));
            }
            vertices.add(vertex);
        }
    }

    /**
     * Optionally returns a {@link Optional Optional&lt;Vertex&gt;} at the specified coordinate.
     * @param c The coordinate to retrieve the vertex from
     * @return An optional containing the vertex if the coordinate is not out of bounds; an empty optional otherwise
     */
    public Optional<Vertex> at(Coord c) {
        if (c.y() < 0 || c.y() >= DEFAULT_WIDTH || c.x() < 0 || c.x() >= DEFAULT_HEIGHT) {
            return Optional.empty();
        }
        return Optional.of(vertices.get(c.y()).get(c.x()));
    }

    /**
     * Gets a copy of vertices of the current game map.
     * @return A copy of the list of vertices
     */
    public List<List<Vertex>> getVertices() {
        return new ArrayList<>(vertices);
    }

    /**
     * Sets the vertex at the coordinates of the given vertex.
     * @param v The vertex to set
     */
    public void set(Vertex v) {
        vertices.get(v.y()).set(v.x(), v);
    }

    /**
     * Writes the object to the specified Path.
     * @param p The path to write to
     * @throws IOException If writing to the path failed
     */
    public void write(Path p) throws IOException {
        Files.write(p, List.of(toCsv(vertices)));
    }

    /**
     * Converts a list of vertices to CSV format.
     * @param vertices The List of vertices to write, ordered by row
     * @return A string containing the formatted CSV
     */
    public static String toCsv(List<List<Vertex>> vertices) {
        return vertices.stream().map(
                vs -> vs.stream().map(v -> v.getTileType().ordinal())
                        .map(i -> Integer.toString(i))
                        .collect(Collectors.joining(", "))
        ).collect(Collectors.joining(",\n"));
    }

    /**
     * Checks if a vertex can be passed by other entities.
     * @param vertex Vertex to be checked
     * @return True if this vertex can be passed, false otherwise
     */
    public boolean isPassable(Vertex vertex) {
        return vertex.getTileType() != TileType.BARRIER;
    }


    /**
     * Adds an entity to the map at the specified vertex.
     * @param vertex The vertex the entity is located at
     * @param entity The entity to add
     * @throws IllegalStateException If the vertex is impassable
     */
    public void addEntity(Vertex vertex, Entity entity) {
        if (!isPassable(vertex)) {
            throw new IllegalStateException("Adding entity " + entity + " to impassable vertex " + vertex.getCoord());
        }
        entities.put(entity, vertex);
    }

    /**
     * Removes an entity from the map.
     * @param entity The entity to remove
     */
    public void removeEntity(Entity entity) {
        entities.remove(entity);
    }

    /**
     * Checks if an entity is in this map.
     * @param entity Entity whose presence in this map is to be tested
     * @return True if it exists, False otherwise
     */
    public boolean hasEntity(Entity entity) {
        return entities.containsKey(entity);
    }

    /**
     * Returns the location (as a Vertex class) of the provided entity.
     * @param entity Entity whose location is wanted
     * @return The vertex the Entity is located at, or null if the Entity is not on the map
     */
    public Vertex entityLocation(Entity entity) {
        return entities.get(entity);
    }

    /**
     * Returns a set view of the entities in this map.
     * Any changes in the map will be reflected here.
     * This returned set is immutable - do not edit it.
     * @return A set view of the underlying entity map
     */
    public Set<Entity> getEntities() {
        return Collections.unmodifiableSet(entities.keySet());
    }

    /**
     * Runs the supplied consumer on all vertices.
     * @param cons The action to perform on each vertex
     */
    public void forAll(Consumer<Vertex> cons) {
        vertices.stream().flatMap(List::stream).forEach(cons);
    }

    /**
     * Linearly interpolates between the starting vertex and the ending vertex.
     * @param start The starting position
     * @param end The ending position
     * @return A list of vertices from the current map which are interpolated
     */
    public List<Vertex> lerp(Vertex start, Vertex end) {
        if (start.equals(end)) {
            return List.of(end);
        }
        Vertex left, right, up, down;
        if (start.x() > end.x()) {
            right = start;
            left = end;
        } else {
            right = end;
            left = start;
        }
        if (start.y() > end.y()) {
            up = start;
            down = end;
        } else {
            up = end;
            down = start;
        }
        List<Vertex> ret = new ArrayList<>();
        int dx = right.x() - left.x();
        if (dx == 0) {
            return IntStream.rangeClosed(down.y(), up.y())
                    .mapToObj(i -> at(new Coord(left.x(), i)))
                    .flatMap(Optional::stream)
                    .collect(Collectors.toList());
        }
        double slope = (double) (right.y() - left.y()) / dx;
        double c = right.y() - right.x() * slope;
        for (int curX = left.x(); curX < right.x(); curX++) {
            double nextStep = Math.abs(slope) <= 1 ? 0.5 : 1;
            int nextY = (int) Math.round(slope * (curX + nextStep) + c);
            int curY = (int) Math.round(slope * curX + c);
            if (nextY == curY) {
                at(new Coord(curX, curY)).ifPresent(ret::add);
            } else {
                IntStream stream;
                if (nextY > curY) {
                    stream = IntStream.range(curY, nextY);
                } else {
                    stream = IntStream.range(nextY, curY);
                }
                int copiedX = curX;
                ret.addAll(stream.mapToObj(i -> at(new Coord(copiedX, i)))
                        .flatMap(Optional::stream)
                        .toList());
            }
        }
        if (slope < -1.0) {ret.add(left);}
        ret.add(right);
        return ret;
    }

    /**
     * Optionally finds the entrance of the map, if it exists.
     * @return An optional containing the entrance of the map; an empty optional otherwise
     */
    public Optional<Vertex> findEntrance() {
        return vertices.stream()
                .flatMap(List::stream)
                .filter(v -> v.getTileType() == TileType.ENTRANCE)
                .findFirst();
    }

    /**
     * Optionally finds the exit of the map, if it exists.
     * @return An optional containing the entrance of the map; an empty optional otherwise
     */
    public Optional<Vertex> findExit() {
        return vertices.stream()
                .flatMap(List::stream)
                .filter(v -> v.getTileType() == TileType.EXIT)
                .findFirst();
    }

    /**
     * Retrieves a List of vertices which are at some straight line distance away from the input.
     * @param other The vertex to look around
     * @param distance The straight line distance
     * @return The List of vertices retrieved
     */
    public List<Vertex> around(Vertex other, int distance) {
        Coord otherCoord = other.getCoord();
        List<Coord> coordinates = List.of(
                otherCoord.translate(new Coord(-distance, 0)),
                otherCoord.translate(new Coord(distance, 0)),
                otherCoord.translate(new Coord(0, -distance)),
                otherCoord.translate(new Coord(0, distance)));
        return coordinates.stream().map(this::at)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MapData mapData)) return false;
        return Objects.equals(vertices, mapData.vertices);
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices);
    }
}
