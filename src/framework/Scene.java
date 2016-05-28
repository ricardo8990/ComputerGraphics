/* YOU SHOULD NOT EDIT THIS FILE */
package framework;

import processing.core.*;			// Processing core libraries
import processing.opengl.*;			// Processing/OpenGL binding
import javax.media.opengl.GL2;		// Advanced JOGL interfaces
import java.util.*;					// Java utility
import framework.objects.*;			// Framework objects (DisplayableObject and AnimatedObject)
/**
 * Default setup for G53GRA Coursework. You should subclass {@code Scene} and add your functionality.
 * Override {@link #initialise()} to add {@link DisplayableObject}s to your {@code Scene}. Running your subclass
 * will setup and draw objects based on your {@link #initialise()} function.
 * <p>
 * <b>Functions that can be overloaded:</b>
 * <br>{@link #initialise()}<br>{@link #setInitWindowSize()}<br>{@link #projection()}<br>{@link #reshape()}
 * <br>{@link #globalLighting()}<br>{@link #update()} <b>(advanced)</b><br>{@link #setupGL()} <b>(advanced)</b>
 * @author wil
 * @version 1.0.0
 */
public class Scene extends PApplet {
	private static final long serialVersionUID = 3922098387294872984L;
	private static final boolean DEBUG = false;
////// VARIABLE DEFINITIONS /////////////////////////////////////////
	/**
	 * Map containing all {@link DisplayableObject}s in the scene.
	 * Keys are identifying {@link String}s, which can be either specified or 
	 * are automatically generated by the system.
	 * @see DisplayableObject
	 * @see #addObjectToScene(DisplayableObject)
	 * @see #addObjectToScene(DisplayableObject, String)
	 * @see #getObject(String)
	 */
	private Map<String, DisplayableObject> objects = new HashMap<String, DisplayableObject>();
	/**
	 * Number of (animated) objects in pushed to Scene
	 */
	protected int numObj, numAni;
	/**
	 * Runtime variable, marks last time {@code dT} was calculated.
	 */
	protected float prevTime;
	/**
	 * <b>ADVANCED</b>
	 * <p>
	 * Progressing-OpenGL abstraction layer. Access only from within {@link setupGL} or in sub-classes
	 * @see #setupGL()
	 */
	public PGL gl;
	/**
	 * Scene camera. Contains viewing properties. Defaults to framework {@link Camera} class.
	 * Overload {@link #initialise()} to set a different {@link Camera}. Remember to access as {@code super.camera}. 
	 */
	protected Camera camera;
	/**
	 * Initial window size.
	 * @see #setInitWindowSize()
	 */
	protected int initWidth, initHeight;
	/**
	 * Clear colour (i.e. background).
	 * @see #setBackgroundColour()
	 */
	private int bgcolour;
////// SETUP FUNCTIONS //////////////////////////////////////////////
	/** 
	 * Default setup method to initialise Scene
	 * <p>
	 * Finds declaration of window size in (overloaded) {@link #setInitWindowSize()} method
	 * and opens a 3D rendering PApplet. Overload {@link #initialise()} to add calls during setup.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 * @see #draw()
	 * @see #initialise()
	 * @see #setInitWindowSize()
	 */
	@Override
	public final void setup(){
		setInitWindowSize();			// Initialise init window size (override this method in sub-class)
		setBackgroundColour();			// Initialise default background (clear) colour 
		size(initWidth,initHeight,P3D);	// Create window (in 3D)
		
		gl = beginPGL();
		setupGL();						// Setup initial Processing/OpenGL properties
		
		initialise();					// Call initialise() (should overload in sub-class)
		prevTime = millis();
		
		System.out.println("Setup complete. Window created ["+width+"x"+height+"]");
		System.out.println("OpenGL version " + gl.getString(PGL.VERSION));
	}
	/**
	 * Default properties of {@link #Scene}. Overload this to add {@link DisplayableObject}s to your
	 * scene. Call {@code super()} in your overloaded function to setup default {@link Camera} and
	 * projection property.
	 * @see #setup()
	 * @see Camera
	 * @see #projection()
	 */
	protected void initialise(){
		camera = new Camera(this);		// Initialise camera
		projection();					// Setup projection
		if(DEBUG) System.out.println("Default initialisation complete.");
	}
	
	/**
	 * Default window size for {@link #Scene}. Overload this method and call set {@code super.initWidth}
	 * and {@code super.initHeight} to your preferred window width and height respectively.
	 * @see #setup()
	 */
	protected void setInitWindowSize(){
		initWidth  = 600;				// Default width:  600px
		initHeight = 400;				// Default height: 400px
	}
	/**
	 * Sets rendering properties of your scene by accessing OpenGL commands directly.
	 * <p>
	 * You should only overload this method if you are confident in your understanding of the PGL binding.
	 * @see #setup()
	 * @see #draw()
	 */
	protected void setupGL(){
		gl.enable(PGL.DEPTH_TEST);		// Make sure depth testing is enabled
		gl.depthFunc(PGL.LEQUAL);		// Set depth function
		gl.enable(PGL.CULL_FACE);		// Enable face culling
		gl.frontFace(PGL.CCW);			// Set counter-clockwise winding
		gl.cullFace(PGL.BACK);			// Set back-face culling
		gl.enable(GL2.GL_NORMALIZE);
		setupGL2();
		
		colorMode(RGB);		 			// Sets the default colour mode to RGB(A)
		textureMode(NORMAL); 			// Normalises texture coordinates so that (0,0) is ...
							 			//  ... the top-left and (1,1) is bottom-right
	}
	/**
	 * Advanced OpenGL settings. 
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 */
	private final void setupGL2(){
		GL2 gl2 = ((PJOGL)gl).gl.getGL2();
		gl2.glEnable(GL2.GL_BLEND);
		gl2.glEnable(GL2.GL_COLOR_MATERIAL);
		gl2.glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA);
		gl2.glLightModeli(GL2.GL_LIGHT_MODEL_TWO_SIDE,0);
		gl2.glShadeModel(GL2.GL_SMOOTH);
	}
	
/////// DRAW FUNCTIONS //////////////////////////////////////////////
	/**
	 * Called directly after {@link #setup()} ends. This function will loop continuously until the
	 * program is exited. The draw loop can be paused by calling {@link #noLoop()} and called manually
	 * with {@link #redraw()}. Calling {@link #loop()} will allow {@code draw()} to run continuously again.
	 * <p>
	 * The frequency at which {@code draw()} is called per second can be controlled by calling
	 * {@link #frameRate(float)}
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 * <p>
	 * {@code draw()} should NEVER be called explicitly.
	 * @see #setup()
	 * @see #reshape()
	 * @see #redraw()
	 * @see #frameRate(float)
	 * @see #loop()
	 * @see #noLoop() 
	 */
	@Override
	public final void draw(){
		gl = beginPGL();
		setupGL();									// setup OpenGL properties
		background(bgcolour);						// set background colour
		resetMatrix();								// load identity
		reshape();									// handle any window resizing
		update();									// update Camera and AnimatedObjects
		camera.setup();								// setup camera
		lights();									// setup lighting
		for(DisplayableObject obj : objects.values())
			obj.display();							// display all objects in the scene
		flush();
	}
	/**
	 * Sets the clear (background) colour of the rendering window. By default, the colour is
	 * black, but can be called with either an int {@code grey} value [0-255], or ({@code R,G,B}) values as an 
	 * either float [0.f-1.f] or int [0-255].
	 * Optionally, the alpha value can be specified as an additional parameter to control transparency.
	 * This should be of the same type as the colour values specified, i.e. float or int.
	 * @see #setBackgroundColour(int g)
	 * @see #setBackgroundColour(float r, float g, float b)
	 * @see #setBackgroundColour(int r, int g, int b)
	 * @see #setBackgroundColour(float r, float g, float b, float a)
	 * @see #setBackgroundColour(int r, int g, int b, int a)
	 */
	protected final void setBackgroundColour(){ bgcolour = color(0); }
	/**
	 * @see #setBackgroundColour()
	 * @param g grey-scale value [0-255]
	 */
	protected final void setBackgroundColour(int g){ bgcolour = color(g); }
	/**
	 * @see #setBackgroundColour()
	 * @param r red colour value [0.f-1.f]
	 * @param g green colour value [0.f-1.f]
	 * @param b blue colour value [0.f-1.f]
	 */
	protected final void setBackgroundColour(float r, float g, float b){ setBackgroundColour(r,g,b,1.f); }
	/**
	 * @see #setBackgroundColour()
	 * @param r red colour value [0.f-1.f]
	 * @param g green colour value [0.f-1.f]
	 * @param b blue colour value [0.f-1.f]
	 * @param a alpha value (transparency) [0.f-1.f]
	 */
	protected final void setBackgroundColour(float r, float g, float b, float a){ bgcolour = color(r,g,b,a); }
	/**
	 * @see #setBackgroundColour()
	 * @param r red colour value [0-255]
	 * @param g green colour value [0-255]
	 * @param b blue colour value [0-255]
	 */
	protected final void setBackgroundColour(int r, int g, int b){ setBackgroundColour(r,g,b,255); }
	/**
	 * @see #setBackgroundColour()
	 * @param r red colour value [0-255]
	 * @param g green colour value [0-255]
	 * @param b blue colour value [0-255]
	 * @param a alpha value (transparency) [0-255]
	 */
	protected final void setBackgroundColour(int r, int g, int b, int a){ bgcolour = color(r,g,b,a); }
	/**
	 * Called during every {@link #draw()} cycle. Intended to handle resizing events.
	 * <p>
	 * You should this function to handle the window being resized. The default property is to update the
	 * projection parameters. You can axis the window size parameters by accessing the {@code width} and {@code height}
	 * variables. For example, you could overload this function to adjust the size of all objects in your {@code Scene}.
	 * @see #draw()
	 * @see #projection()
	 * @see #initialise()
	 */
	protected void reshape(){
		projection();		// setup projection
	}
	/**
	 * Adds a {@link DisplayableObject} (includes {@link AnimatedObject}s) to the {@link Scene}.
	 * Generates automatic key for {@code objects} map based on number in scene.
	 * Automatically generated keys are of the form:
	 * <p>
	 * {@code "obj_[i-1]"} for the {@code i}th {@link DisplayableObject} added to the {@link Scene}
	 * (counts {@link AnimatedObject}s).
	 * <p>
	 * {@code "ani_[i-1]"} for the {@code i}th {@link AnimatedObject} added to the {@link Scene}.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 * @see #addObjectToScene(DisplayableObject, String)
	 * @see #getObject(String)
	 * @param obj DisplayableObject to be added to the scene.
	 */
	protected final void addObjectToScene(DisplayableObject obj){
		String autogenid;
		if(obj instanceof AnimatedObject)
			autogenid = "ani_" + numAni;			// Generate ID for AnimatedObject
		else
			autogenid = "obj_" + numObj;			// Generate ID for DisplayableObject
		addObjectToScene(obj, autogenid);
	}
	/**
	 * Adds a DisplayableObject (includes AnimatedObjects) to the {@link Scene}.
	 * String provided is key for {@code objects} map.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 * @see #addObjectToScene(DisplayableObject)
	 * @see #getObject(String)
	 * @param obj DisplayableObject to be added to the scene
	 * @param id String value for accessing the {@code objects} map
	 */
	protected final void addObjectToScene(DisplayableObject obj, String id){
		if(obj instanceof AnimatedObject)
			numAni++;								// Increment count of AnimatedObjects
		objects.put(id, obj);						// Add to map
		numObj++;									// Increment count of all Objects
		if(DEBUG) System.out.println("Added object #" + numObj + " to Scene. ID " + id);
	}
	/**
	 * Access the map of {@link DisplayableObject}s.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 * @param id identifier of object
	 * @return The {@link DisplayableObject} value contained at key {@code id}
	 */
	protected final DisplayableObject getObject(String id){
		DisplayableObject obj = objects.get(id);
		return obj;
	}
//////PROJECTION FUNCTIONS /////////////////////////////////////////
	/**
	 * Sets the projection properties of the scene. Override to change projection properties.
	 * <p>
	 * Default: Orthographic mode
	 */
	protected void projection() {
		ortho(-width/2.f,width/2.f,-height/2.f,height/2.f,1.f,4000.f);
	}
////// ANIMATION FUNCTIONS //////////////////////////////////////////
	/**
	 * The update function for {@link Camera} and {@link AnimatedObject}s. Calculates the time-delay since
	 * the last update and passes as a parameter to the respective class's {@code update()} functions.
	 * <p>
	 * You should only override this class if you want to change how the animation update function works.
	 * This is not advised.
	 * @see Camera
	 * @see AnimatedObject
	 * @see #draw()
	 * @see #runtime()
	 */
	protected void update(){
		float dT = runtime();						// Calculate runtime since last call
		camera.update(dT);							// Update camera
		for(DisplayableObject obj : objects.values()){	// Look for AnimatedObjects
			if(obj instanceof AnimatedObject) ((AnimatedObject) obj).update(dT); // update Animation
		}
	}
	/**
	 * Calculates time delay between updates and returns difference in seconds.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 * @return dT (ΔT) the difference in time since the last call to {@code runtime}
	 */
	protected final float runtime(){
		int currTime = millis();						// Get total runtime in milliseconds
		float dT = (float)(currTime-prevTime)/1000.f;	// Calculate time difference and convert to seconds 
		prevTime = currTime;							// Update internal time variable
		return dT;
	}
////// LIGHTING FUNCTIONS ///////////////////////////////////////////
	/**
	 * Sets default lighting properties and applies all lighting to the scene.
	 * <p>
	 * Enables lighting in all objects that implement {@link Lighting}, after {@link #globalLighting()} is called.
	 * @see #noLights()
	 * @see Lighting#setupLighting()
	 * @see #globalLighting()
	 * @see #resetLightProperties()
	 */
	public final void lights(){
		camera.setup();									// Check camera is correct 
		resetLightProperties();							// Reset specular colour and light attenuation (stacks) 
		globalLighting();								// Set global Scene lighting
		for(DisplayableObject obj : objects.values()){	// Look for lit objects
			resetLightProperties();						// Reset specular and attenuation for each light 
			if(obj instanceof Lighting) ((Lighting) obj).setupLighting();	// Setup lights for objects that implement Lighting
		}
	}
	/**
	 * Default globalLighting. Sets up ambient lighting for the whole scene.
	 * Override to change global lighting.
	 * @see #lights()
	 */
	protected void globalLighting(){
		ambientLight(245.f, 255.f, 255.f);				// Global ambient lighting colour
	}
	/**
	 * Resets default lighting properties. These properties only affect lighting commands that come after they are called.
	 * <p>
	 * Sets default specular colour to white [255,255,255].<br>
	 * Sets default light attenuation to constant (non-decaying).
	 * @see #globalLighting()
	 * @see #lights()
	 */
	protected final void resetLightProperties(){
		lightSpecular(255.f, 255.f, 255.f);				// Default: white specular light
		lightFalloff(1.f, 0.f, 0.f);					// Default: constant fall-off, no decay
	}
	
////// KEY HANDLING FUNCTIONS ///////////////////////////////////////
	/**
	 * Called when a key is pressed.
	 * Indicates whether key pressed is ASCII or coded, and relays key information
	 * and mouse coordinates to {@link Camera} and any {@link DisplayableObject}s
	 * that implement {@link Input}.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 */
	@Override
	public final void keyPressed(){
		if(key == CODED){								// Special key (non ASCII)
			camera.handleSpecialKey(keyCode, 1, mouseX, mouseY);	// Pass to camera
			for(DisplayableObject obj : objects.values())			// Pass to objects that implement Input
				if(obj instanceof Input) ((Input) obj).handleSpecialKey(keyCode, 1, mouseX, mouseY);
		} else {										// ASCII key
			camera.handleKey(key, 1, mouseX, mouseY);				// Pass to camera
			for(DisplayableObject obj : objects.values())			// Pass to objects that implement Input
				if(obj instanceof Input) ((Input) obj).handleKey(key, 1, mouseX, mouseY);
		}
		if(DEBUG) System.out.println("Key Pressed");
	}
	/**
	 * Called when a key is released.
	 * Indicates whether key released is ASCII or coded, and relays key information
	 * and mouse coordinates to {@link Camera} and any {@link DisplayableObject}s
	 * that implement {@link Input}.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 */
	@Override
	public final void keyReleased(){
		if(key == CODED){								// Special key (non ASCII)
			camera.handleSpecialKey(keyCode, 0, mouseX, mouseY);	// Pass to camera
			for(DisplayableObject obj : objects.values())			// Pass to objects that implement Input
				if(obj instanceof Input) ((Input) obj).handleSpecialKey(keyCode, 0, mouseX, mouseY);
		} else {										// ASCII key
			camera.handleKey(key, 0, mouseX, mouseY);				// Pass to camera
			for(DisplayableObject obj : objects.values())			// Pass to objects that implement Input
				if(obj instanceof Input) ((Input) obj).handleKey(key, 0, mouseX, mouseY);
		}
		if(DEBUG) System.out.println("Key Released");
	}
	
////// MOUSE HANDLING FUNCTIONS /////////////////////////////////////
	/**
	 * Called when mouse is moved while a button pressed.
	 * Relays coordinates to {@link Camera} and any {@link DisplayableObject}s that implement {@link Input}.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 */
	@Override
	public final void mouseDragged(){
		camera.handleMouseDrag(mouseX, mouseY);						// Pass to camera
		for(DisplayableObject obj : objects.values())				// Pass to objects that implement Input
			if(obj instanceof Input) ((Input) obj).handleMouseDrag(mouseX, mouseY);
		if(DEBUG) System.out.println("Mouse Dragged: (" + mouseX + "," + mouseY + ")");
		
	}
	/**
	 * Called when mouse is moved while no button pressed.
	 * Relays coordinates to {@link Camera} and any {@link DisplayableObject}s that implement {@link Input}.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 */
	@Override
	public final void mouseMoved(){
		camera.handleMouseMove(mouseX, mouseY);						// Pass to camera
		for(DisplayableObject obj : objects.values())				// Pass to objects that implement Input
			if(obj instanceof Input) ((Input) obj).handleMouseMove(mouseX, mouseY);
		if(DEBUG) System.out.println("Mouse moved: (" + mouseX + "," + mouseY + ")");
	}
	/**
	 * Called when a mouse button is pressed.
	 * Relays the button pressed (LEFT, RIGHT or CENTER), and the coordinates to {@link Camera}
	 * and any {@link DisplayableObject}s that implement {@link Input}.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 */
	@Override
	public final void mousePressed(){
		camera.handleMouse(mouseButton, 1, mouseX, mouseY);			// Pass to camera
		for(DisplayableObject obj : objects.values())				// Pass to objects that implement Input
			if(obj instanceof Input) ((Input) obj).handleMouse(mouseButton, 1, mouseX, mouseY);
		if(DEBUG) System.out.println("Mouse Pressed: (" + mouseX + "," + mouseY + ")");
	}
	/**
	 * Called when a mouse button is released.
	 * Relays the button released (LEFT, RIGHT or CENTER), and the coordinates to {@link Camera}
	 * and any {@link DisplayableObject}s that implement {@link Input}.
	 * <p>
	 * It is strongly recommended you do NOT attempt to override this method.
	 */
	@Override
	public final void mouseReleased(){
		camera.handleMouse(mouseButton, 0, mouseX, mouseY);			// Pass to camera
		for(DisplayableObject obj : objects.values())				// Pass to objects that implement Input
			if(obj instanceof Input) ((Input) obj).handleMouse(mouseButton, 0, mouseX, mouseY);
		if(DEBUG) System.out.println("Mouse Released: (" + mouseX + "," + mouseY + ")");
	}
}
