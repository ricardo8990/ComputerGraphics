package coursework.objects;

import processing.core.PApplet;
import processing.core.PImage;
import processing.opengl.PGL;
import framework.Scene;
import framework.objects.DisplayableObject;

public class PalmTree extends DisplayableObject 
{
	boolean useTextures = false;
	PImage barkText;
	PImage leaveText;
	
	public PalmTree(Scene parent, PImage bark, PImage leave) 
	{
		super(parent);
		useTextures = bark != null && leave != null;
		barkText = bark;
		leaveText = leave;
	}

	@Override
	public void display() 
	{
		parent.pushMatrix();
		parent.pushStyle();
		
		parent.translate(pos.x,pos.y,pos.z);			
		parent.scale(scale.x,scale.y,scale.z);			
		parent.rotateY(rotation.y);						
		parent.rotateZ(rotation.z);						
		parent.rotateX(rotation.x);						
		
		trunk();
		leaves();
		
		parent.popStyle();
		parent.popMatrix();
	}
	
	private void trunk()
	{
		parent.pushStyle();
		parent.noStroke();
		parent.fill(Constants.trunkColour[0], Constants.trunkColour[1], Constants.trunkColour[2]);
		parent.ambient(Constants.trunkColour[0], Constants.trunkColour[1], Constants.trunkColour[2]);
		parent.specular(0, 0, 0);
		
		float res = (float)Math.PI*0.1f;         
        float r = Constants.TRUNK_RADIUS;        
        float x = r, z = 0.f;                   
        float t = 0.f;                          
        
        do{
			parent.beginShape(PApplet.QUADS);
			if(useTextures)
				parent.texture(barkText);
			//Add normal
			parent.normal(x, 0.f, z);
	        // Create first points
	        parent.vertex(x, 0.f, z, 0, 0);     
	        parent.vertex(x, -1.f, z, 0, 1);    
	        // Iterate around circle
	        t += res;                       
	        x = r*(float)Math.cos(t);       
	        z = r*(float)Math.sin(t);
	        // Close quad
	        parent.normal(x, 0.f, z);
	        parent.vertex(x, -1.f, z, 1, 1);
	        parent.vertex(x, 0.f, z, 1, 0); 
	        parent.endShape();
        } while(t <= 2 *(float)Math.PI);
        
        parent.translate(0.f, -1.f, 0.f);
        parent.sphere(-r);
        parent.popStyle();
	}
	
	private void leaves()
	{
		parent.pushStyle();
		parent.noStroke();
		parent.fill(Constants.leaveColour[0], Constants.leaveColour[1], Constants.leaveColour[2]);
		parent.ambient(Constants.leaveColour[0], Constants.leaveColour[1], Constants.leaveColour[2]);
		parent.specular(0, 0, 0);
		PGL pgl = parent.beginPGL();
		pgl.enable(PGL.CULL_FACE);
		float t = 0.f;
		float r = Constants.TRUNK_RADIUS;
		float x = r, z = 0.f;
		float res = (float) ((2 * Math.PI) / Constants.NUMBER_OF_LEAVES);
		
		do
		{			
			parent.beginShape(PApplet.TRIANGLE);
			
			if(useTextures)
				parent.texture(leaveText);
			
			parent.vertex(x, 0.f, z, 0, 0);
			
			x = (r + Constants.LEAVE_RADIUS) * (float)Math.cos(t + (Constants.TRUNK_WIDTH / 2));
			z = (r + Constants.LEAVE_RADIUS) * (float)Math.sin(t + (Constants.TRUNK_WIDTH / 2));
			
			parent.vertex(x, 0.3f, z, 1, 1);
			
			x = (r + (Constants.LEAVE_RADIUS * 0.1f)) * (float)Math.cos(t + Constants.TRUNK_WIDTH);
			z = (r + (Constants.LEAVE_RADIUS * 0.1f)) * (float)Math.sin(t + Constants.TRUNK_WIDTH);
			
			parent.vertex(x, 0.f, z, 0, 1);		
			
			t += res;                       
	        x = (r + (Constants.LEAVE_RADIUS * 0.1f)) * (float)Math.cos(t);       
	        z = (r + (Constants.LEAVE_RADIUS * 0.1f)) * (float)Math.sin(t);
			
			parent.endShape();
		}while(t <= 2 * ((float) Math.PI));
		parent.popStyle();
		pgl.disable(PGL.CULL_FACE);
		parent.endPGL();
	}

}
