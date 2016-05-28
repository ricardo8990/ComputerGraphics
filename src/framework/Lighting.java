/* YOU SHOULD NOT EDIT THIS FILE */
package framework;
/**
 * Interface for giving an coursework object lighting properties. Any class you want to have a light source, e.g. a Torch
 * should implement the {@code Lighting} interface. e.g. {@code public Class implements Lighting} 
 * <p>
 * In {@link Scene}, {@link #setupLighting()} will be called on any {@linkplain framework.objects.DisplayableObject DisplayableObject}
 * added to the scene that {@code implements Lighting}, from the {@linkplain Scene#lights() lights()} method.
 * @author wil
 * @version 1.0.0
 */
public interface Lighting {
	/**
	 * Contains all lighting commands for your object. Called via {@link Scene#lights()}. Remember to give lighting positions in terms
	 * of World Space.
	 */
	void setupLighting();
	
}
