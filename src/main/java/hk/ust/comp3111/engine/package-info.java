/**
 * Defines the components contains the classes that handle the inner workings of the game.
 *
 * <p>
 *     The {@link hk.ust.comp3111.engine.Entity} class is an abstract base class that contains all necessary data needed for
 *     a moving component of the game.
 * </p>
 * <p>
 *     The classes {@link hk.ust.comp3111.engine.Tom}, {@link hk.ust.comp3111.engine.Jerry}
 *     and {@link hk.ust.comp3111.engine.Crystal} are components of the actual gameplay, which all inherits the abstract class
 *     {@link hk.ust.comp3111.engine.Entity}.
 * </p>
 * <p>
 *     The {@link hk.ust.comp3111.engine.GameStatus} enum class contains values to indicate the game status of the game.
 * </p>
 * <p>
 *     The {@link hk.ust.comp3111.engine.ImagePath} enum class links the state of the components to the appropriate image path.
 * </p>
 * <p>
 *     The {@link hk.ust.comp3111.engine.Engine} is the class that controls the game, what is done per tick, and all the logics behind the game.
 * </p>
 */
package hk.ust.comp3111.engine;