package hk.ust.comp3111.algo.shortestpath;

import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Class for the shortest path algorithm BFS. Used by maze visualizer, and Tom in the game loop.
 */
public class BreadthFirstSearch {

    private final Random rand = new Random();
    /**
     * A node in BFS graph. Contains its vertex, previous node, and adjacent vertices.
     */
    private static class GraphNode {

        private final Vertex current;
        private GraphNode parent;
        private boolean visited;
        private final List<Vertex> adjacents;

        /**
         * Constructs a new GraphNode object.
         * @param vertex The current Vertex of the node.
         * @param data The map data for which to detect adjacent vertices.
         */
        public GraphNode(Vertex vertex, MapData data) {
            this.current = vertex;
            this.parent = null;
            this.visited = false;
            this.adjacents = data.around(vertex, 1).stream()
                    .filter(data::isPassable)
                    .collect(Collectors.toList());
        }

    }

    /**
     * The actual BFS algorithm.
     * @param data The map data of the maze.
     * @param start The Vertex at which to start searching from.
     * @param end The Vertex at which the searching ends.
     * @param shortestPath Toggle whether BFS stops at exit (True) or after traversing every adjacent node (False)
     * @return A list of Vertex of the shortest path, from start to end.
     */
    public List<Vertex> bfsGeneral(MapData data, Vertex start, Vertex end, boolean shortestPath) {

        // if start or end is blocked then return Empty List
        if (!data.isPassable(start) || !data.isPassable(end)) {
            return List.of();
        }

        // establish graph, queue, path
        Map<Vertex, GraphNode> graph = data.getVertices().stream() // new HashMap<>();
                .flatMap(List::stream)
                .filter(v -> (v != start && data.isPassable(v)))
                .collect(Collectors.toMap(Function.identity(), v -> new GraphNode(v, data)));

        LinkedList<GraphNode> queue = new LinkedList<>();
        List<Vertex> route = new ArrayList<>();
        boolean pathFound = false;

        // traverse graph
        graph.put(start, new GraphNode(start, data));
        queue.add(graph.get(start));

        while (!queue.isEmpty()) {
            // get first node
            GraphNode currentNode = queue.removeFirst();

            // if current node is exit then exit loop
            if (currentNode.current.equals(end)) {
                pathFound = true;
                break;
            }

            // otherwise continue to update graph
            for (Vertex v : currentNode.adjacents) {
                GraphNode it = graph.get(v);
                if (!it.visited) {
                    if (!shortestPath) {route.add(v);}
                    it.parent = currentNode;
                    it.visited = true;
                    queue.add(graph.get(v));
                }
            }
        }

        // return the desired path
        if (shortestPath) {

            // if the entrance and exit are not connected on the graph, then there is no possible path
            if (!pathFound) {return List.of();}

            // get path by back traversal
            GraphNode pathIter = graph.get(end);

            while (!pathIter.current.equals(start)) {
                route.add(pathIter.current);
                pathIter = pathIter.parent;
            }
            route.add(start);
            Collections.reverse(route);
        }

        return route;
    }

    /**
     * Gets the next Vertex to move to. Used by Tom in game loop.
     * @param data The map data of the maze.
     * @param start The Vertex at which to start searching from.
     * @param end The Vertex at which the searching ends.
     * @return The next optimal Vertex to move to.
     */
    public Vertex nextShortestPathVertex(MapData data, Vertex start, Vertex end) {
        List<Vertex> ret = bfsGeneral(data, start, end, true);
        return (ret.size() <= 1) ? start : ret.get(1);
    }

    /**
     * Gets a random Vertex in the playable area of the maze. Used to update Crystal in game loop.
     * @param data The map data of the maze.
     * @param start The Vertex at which to start searching from.
     * @param end The Vertex at which the searching ends.
     * @return A random reachable Vertex.
     */
    public Vertex randomReachableVertex(MapData data, Vertex start, Vertex end) {
        List<Vertex> ret = bfsGeneral(data, start, end, false).stream().filter(v -> (v.getTileType() == Vertex.TileType.CLEAR)).toList();
        return (ret.size() <= 1) ? start : ret.get(rand.nextInt(ret.size()));
    }

}
