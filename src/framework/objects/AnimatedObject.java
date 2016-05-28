package framework.objects;

import framework.Scene;
/**
 * Abstract class to be inherited by all animated objects to be displayed in Scene
 * <p>
 * Contains abstract {@link #display} method that must be overloaded. {@link #display()} is called from a parent {@link Scene}.
 * <p>
 * Contains abstract {@link #update} method that must be overloaded. {@link #update(float dT)} is called from a parent {@link Scene}. 
 * @see DisplayableObject
 * @author wil
 * @version 1.0.0
 */
public abstract class AnimatedObject extends DisplayableObject {
    /**
     * Constructor for AnimatedObject that sets up link to rendering window commands.
     * <p>
     * Abstract class so constructor should only be called by sub-class via {@code super(parent)}
     * @see AnimatedObject
     * @param parent Your {@link Scene}.
     */
	public AnimatedObject(Scene parent) {
		super(parent); // Calls DisplayableObject constructor
	}
	/**
	 * Called each frame to update
	 * <p>
	 * Use this to update animation sequence.
	 * @param dT (∆T) change in time since previous call
	 */
	public abstract void update(float dT);
}
