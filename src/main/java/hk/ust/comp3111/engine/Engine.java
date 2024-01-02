package hk.ust.comp3111.engine;

import hk.ust.comp3111.algo.shortestpath.BreadthFirstSearch;
import hk.ust.comp3111.api.Coord;
import hk.ust.comp3111.api.MapData;
import hk.ust.comp3111.api.Vertex;
import hk.ust.comp3111.ui.Direction;

import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;

/**
 * Engine, the class responsible for the game loop.
 */
public class Engine {
    private final MapData mapData;
    private final Tom tom;
    private final Jerry jerry;
    private final Crystal crystal;
    private final ImagePath[] imagePathValues = ImagePath.values();
    private final Vertex entryPoint;
    private final Vertex exitPoint;
    private Direction direction = Direction.NONE;
    private final BreadthFirstSearch bfsPath = new BreadthFirstSearch();
    private int tomCount;
    private static int TOM_UPDATE_RATE = 4;

    /**
     * Constructor of the Engine class.
     * @param imagePaths A Map of Path object that leads to the image file containing the sprites of Entity classes
     */
    public Engine(MapData mapData, Map<ImagePath, Path> imagePaths) {

        // Other data members
        this.mapData = mapData;
        this.tom = new Tom(imagePaths, ImagePath.TOM_LEFT);
        this.jerry = new Jerry(imagePaths, ImagePath.JERRY_LEFT);
        this.crystal = new Crystal(imagePaths, ImagePath.CRYSTAL);
        this.tomCount = 0;

        // If there is somehow no entry/exit point in the map
        entryPoint = mapData.findEntrance().orElseThrow(() -> new IllegalStateException("The map does not have an entry point."));
        exitPoint = mapData.findExit().orElseThrow(() -> new IllegalStateException("The map does not have an exit point."));
        mapData.addEntity(entryPoint, jerry);
        mapData.addEntity(exitPoint, tom);
        // Add crystal to the map
        mapData.addEntity(bfsPath.randomReachableVertex(mapData, this.entryPoint, this.exitPoint), crystal);
    }

    /**
     * A method to get the map of the class.
     * @return the MapData object of the Engine class
     */
    public MapData getMapData() {
        return mapData;
    }

    /**
     * A mutator to change the direction Jerry is moving towards.
     * @param dir the input Direction enum value
     */
    public void changeDirection(Direction dir){
        this.direction = dir;
    }

    /**
     * The actual gameplay.
     * Supposed to be updated per tick of a game.
     * @return A {@link GameStatus} value that indicates the state of the game
     */
    public GameStatus tick() {
        Direction dir = this.direction;
        this.direction = Direction.NONE;
        // Move Jerry
        if (dir != Direction.NONE){
            Vertex jerryLocation = mapData.entityLocation(jerry);
            Optional<Vertex> toMove = Optional.empty();
            switch (dir) {
                case UP -> toMove = mapData.at(new Coord(jerryLocation.x(), jerryLocation.y() - 1));
                case DOWN -> toMove = mapData.at(new Coord(jerryLocation.x(), jerryLocation.y() + 1));
                case LEFT -> toMove = mapData.at(new Coord(jerryLocation.x() - 1, jerryLocation.y()));
                case RIGHT -> toMove = mapData.at(new Coord(jerryLocation.x() + 1, jerryLocation.y()));
            }
            toMove.filter(mapData::isPassable).ifPresent(p -> moveEntity(p, jerry, dir));
        }

        // Check if Jerry is at the exit
        if (exitPoint.equals(mapData.entityLocation(jerry))) {
            TOM_UPDATE_RATE = 4;
            int increment = ImagePath.JERRY_WIN_LEFT.ordinal() - ((jerry.getCrystal() > 0) ? ImagePath.JERRY_LEFT_CRYSTAL.ordinal() : ImagePath.JERRY_LEFT.ordinal());
            jerry.changeImage(imagePathValues[jerry.getImagePath().ordinal() + increment]);
            return GameStatus.JERRY_WINS;
        }


        // Check if Jerry is at same location as Crystal
        if (mapData.hasEntity(crystal) && mapData.entityLocation(crystal).equals(mapData.entityLocation(jerry))){
            jerry.activateCrystal();
            --TOM_UPDATE_RATE;
            mapData.removeEntity(crystal);
            // Change Jerry sprite
            jerry.changeImage(imagePathValues[jerry.getImagePath().ordinal() + 2]);
        }

        // Check if Tom == Jerry
        if (mapData.entityLocation(tom).equals(mapData.entityLocation(jerry))){
            TOM_UPDATE_RATE = 4;
            // Jerry wins
            if (jerry.getCrystal() > 0) {
                jerry.changeImage(imagePathValues[jerry.getImagePath().ordinal() + 4]);
                return GameStatus.JERRY_WINS;
            }
            // Tom wins
            // Change image to Tom winning
            tom.changeImage(imagePathValues[tom.getImagePath().ordinal() + 2]);
            return GameStatus.TOM_WINS;
        }


        if (tomCount % TOM_UPDATE_RATE != 0) {
            // Get next vertex of Tom
            Vertex newTom;
            if (jerry.getCrystal() > 0)
                newTom = bfsPath.nextShortestPathVertex(mapData, mapData.entityLocation(tom), this.entryPoint);
            else
                newTom = bfsPath.nextShortestPathVertex(mapData, mapData.entityLocation(tom), mapData.entityLocation(jerry));

            // Check Tom's new vertex
            Vertex oldTom = mapData.entityLocation(tom);
            if (oldTom.equals(newTom) && !mapData.entityLocation(tom).equals(entryPoint)) throw new IllegalStateException("Either Tom or Jerry should have won.");

            // NONE is set as default because no matter up or down the sprite does not change,
            // and the value does not affect Tom in moveEntity()
            Direction tomDir = Direction.NONE;
            if (newTom.x() > oldTom.x())
                tomDir = Direction.RIGHT;
            else if (newTom.x() < oldTom.x())
                tomDir = Direction.LEFT;

            // Move Tom
            moveEntity(newTom, tom, tomDir);
        }

        // Change image when crystal == 1,so after the decrease later it resets the image
        if (jerry.getCrystal() == 1) {
            jerry.changeImage(imagePathValues[jerry.getImagePath().ordinal() - 2]);
            TOM_UPDATE_RATE = 4;
        }

        // Respawn crystal if crystal == 0, and it doesn't exist on the map
        // Note that it won't immediately respawn because counter is decreased after this statement
        if (!mapData.hasEntity(crystal) && jerry.getCrystal() == 0){
            // Get crystal location from algo
            mapData.addEntity(bfsPath.randomReachableVertex(mapData, this.entryPoint, this.exitPoint), crystal);
        }

        // Decrease Jerry counter
        jerry.decreaseCounter();

        ++tomCount;
        tomCount %= TOM_UPDATE_RATE;
        // The game is ongoing
        return GameStatus.ONGOING;
    }

    private void moveEntity(Vertex toMove, Entity entity, Direction dir){
        mapData.removeEntity(entity);
        mapData.addEntity(toMove, entity);
        if (entity instanceof Jerry){
            switch (dir){
                case LEFT -> {
                    ImagePath change = ((Jerry) entity).getCrystal() > 0 ? ImagePath.JERRY_LEFT_CRYSTAL : ImagePath.JERRY_LEFT;
                    entity.changeImage(change);
                }
                case RIGHT -> {
                    ImagePath change = ((Jerry) entity).getCrystal() > 0 ? ImagePath.JERRY_RIGHT_CRYSTAL : ImagePath.JERRY_RIGHT;
                    entity.changeImage(change);
                }
            }
            return;
        }
        if (entity instanceof Tom){
            switch (dir){
                case LEFT -> entity.changeImage(ImagePath.TOM_LEFT);
                case RIGHT -> entity.changeImage(ImagePath.TOM_RIGHT);
            }
        }
    }
}