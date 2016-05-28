package coursework;

import java.awt.Container;
import java.io.File;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import processing.core.*;		// import processing core
import framework.*;				// import framework classes
import coursework.objects.*;	// import your objects package
import coursework.objects.Character;
/**
 * Your main Coursework class. Inherits {@code framework.}{@link Scene}. Edit this class.  
 * @author {your_name}
 * @version 1.0.0
 */
@SuppressWarnings("serial")		// suppress warning about serializable uid
public class MyScene extends Scene
{
	/**
	 * Setup your {@code Scene} in this method. Create any {@code DisplayableObject}s and {@code AnimatedObject}s
	 * and add them to the {@code Scene}.
	 * <p>
	 * Call {@code super.initialise()} to setup the framework's default  {@link Camera} and {@linkplain Scene#projection() projection} settings.
	 * @see DisplayableObject
	 * @see AnimatedObject
	 * @see Camera
	 * @see Input
	 */
	
	@Override
	public void initialise()
	{		
		setBackgroundColour(255);
		textureMode(NORMAL);
		
		super.camera = new CustomCamera(this);
		
		PImage bark = loadImage(Constants.TextureBark);
		PImage leave = loadImage(Constants.TextureLeave);
		for(int r = 0; r < Constants.numberOfPalmTrees; r++)
		{
			PalmTree palm = new PalmTree(this, bark, leave);
			palm.size(Constants.palmSize);
			float x = ((float) Math.random() * Constants.endXValue) - Constants.startingXValue;
			float z = ((float) Math.random() * Constants.endYValue) - Constants.startingYValue;
			palm.position(x, 0.f, z);
			addObjectToScene(palm, "palm" + r);
		}
		
		Character character = new Character(this);
		character.size(Constants.characterSize);
		character.position(Constants.charXPos, Constants.charXPos, Constants.charXPos);
		addObjectToScene(character, "char");
		
		PImage skybox[] = new PImage[6];
        skybox[0] = loadImage(Constants.SkyboxLeft);
        skybox[1] = loadImage(Constants.SkyboxRight);
        skybox[2] = loadImage(Constants.SkyboxFront);
        skybox[3] = loadImage(Constants.SkyboxBack);
        skybox[4] = loadImage(Constants.SkyboxDown);
        skybox[5] = loadImage(Constants.SkyboxUp);
		Stage stage = new Stage(this);
		stage.setTextures(skybox);
		stage.size(Constants.stageSize);
		stage.position(0, height / 2,0);
        addObjectToScene(stage, "stage");
        
        //Initialize music
        String file = Constants.fileMusic;
        try
		{
			AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File(file).getAbsoluteFile());
			Clip clip = AudioSystem.getClip();
			clip.open(audioInputStream);
			clip.start();
		} catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	/**
	 * Override default global lighting.
	 * @see #lights()
	 */
	@Override
	protected void globalLighting()
	{
		// Set lighting effect colours and directional parameter    
	    ambientLight(Constants.ambience[0], Constants.ambience[1], Constants.ambience[2]);    // set ambient lighting in the scene
	    lightSpecular(Constants.spec_col[0], Constants.spec_col[1], Constants.spec_col[2]);   // set specular parameter of following lights
	    directionalLight(Constants.diffuse[0], Constants.diffuse[1], Constants.diffuse[2],    // create directional diffuse lighting
	    		Constants.direction[0], Constants.direction[1], Constants.direction[2]);
	}
	
	/**
	 * Override default reshape function. Called during every iteration of {@link #draw()}.
	 * Use this method to handle resizing objects based on your window size.
	 * @see #getObject(String)
	 * @see #projection()
	 */
	protected void reshape()
	{
		super.reshape();
	}
	
	/**
	 * Override default initial window size (600x400). Adjust variables in {@code super} class to change values.
	 */
	@Override
	protected void setInitWindowSize()
	{
		super.initWidth = 800;	// must override variables in super class to affect size
		super.initHeight = 700;
	}
	
	/**
	 * Override projection properties here. Remove call to {@code super.projection()} and replace with
	 * perspective mode.
	 * @see #perspective(float, float, float, float)
	 * @see #ortho(float, float, float, float, float, float)
	 * @see #frustum(float, float, float, float, float, float)
	 */
	@Override
	protected void projection()
	{
		if(Constants.useOrthogonalProjection)
			perspective(radians(60.f), (float) width / (float) height, 1.f, 3000.f);
		else
			super.projection();
	}
	
}
