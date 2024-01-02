/**
 * Classes defining the UI layer of the application.
 *
 * <p>
 * Since the application only creates a single JFrame ({@link hk.ust.comp3111.ui.MainFrame}), all of
 * the UIs are defined in terms of {@link javax.swing.JPanel} subclasses.
 * </p>
 *
 * <p>
 * As {@link javax.swing.JPanel}s do not have the ability to hint to their parent which layout they prefer,
 * the JPanel subclass {@link hk.ust.comp3111.ui.ConstrainedPanel} provides an additional method
 * ({@link hk.ust.comp3111.ui.ConstrainedPanel#getConstraint()}) to hint to the main application frame
 * about how the panel should be laid out. All other panels extend from this class in order to benefit
 * from this layout flexibility.
 * </p>
 *
 * <p>
 * The package also defines a custom UI component, {@link hk.ust.comp3111.ui.MapComponent}, which is reused
 * in multiple UIs to provide a uniform look for map display. It wraps {@link hk.ust.comp3111.api.MapData} instances
 * by associating each vertex with its own JPanel. To render the changes to the JPanel after changing the internal
 * MapData instance, use the {@link hk.ust.comp3111.ui.MapComponent#render()} method, which should be called on the
 * Event Dispatch Thread of Swing. If any code accesses this method from another custom thread (listeners are exempt
 * from this requirement as they already run on said thread), use {@link javax.swing.SwingUtilities#invokeLater(java.lang.Runnable)}
 * to prevent threading issues.
 * </p>
 *
 * @since 0.1
 * @version 1.0
 */
package hk.ust.comp3111.ui;