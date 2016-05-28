package coursework.objects;

import framework.Scene;
import framework.objects.DisplayableObject;
import processing.core.PApplet;
import processing.core.PImage;

public class Stage extends DisplayableObject 
{
	private boolean toTexture = false;
	private PImage tex[] = new PImage[6];
	
	public void setTextures(PImage tex[])
	{
		this.tex = tex;
		toTexture = true;
		for (int r = 0; r < 6; r++)
		{
			if(tex[r] == null)
				toTexture = false;
		}
	}

	public Stage(Scene parent) 
	{
		super(parent);
	}

	@Override
	public void display() 
	{
		parent.pushMatrix();
		parent.pushStyle();
		parent.translate(pos.x, pos.y, pos.z);
		parent.scale(scale.x, scale.y, scale.z);
		parent.fill(255);
		parent.rotateX(rotation.x);
		parent.rotateY(rotation.y);
		parent.rotateZ(rotation.z);
		
		//parent.noFill();
		parent.stroke(0.f);
		parent.strokeWeight(2 / scale.x);
		drawStage();
		parent.popStyle();
		parent.popMatrix();
	}
	
	private void drawStage()
	{
		if(toTexture)
			parent.noStroke();
		parent.noLights();
		
		parent.beginShape(PApplet.QUADS);
        // LEFT SIDE
			if(toTexture) parent.texture(tex[0]);
            parent.vertex(-1.f, -1.f, -1.f, 1, 0);
            parent.vertex(-1.f, -1.f, 1.f, 0, 0);
            parent.vertex(-1.f, 0.f, 1.f, 0, 1);
            parent.vertex(-1.f, 0.f, -1.f, 1, 1);
        parent.endShape();
        parent.beginShape(PApplet.QUAD);
        //    RIGHT SIDE
        	if(toTexture) parent.texture(tex[1]);
            parent.vertex(1.f, -1.f, 1.f, 1, 0);
            parent.vertex(1.f, -1.f, -1.f, 0, 0);
            parent.vertex(1.f, 0.f, -1.f, 0, 1);
            parent.vertex(1.f, 0.f, 1.f, 1, 1);
        parent.endShape();
        parent.beginShape(PApplet.QUAD);
        // FAR SIDE
        	if(toTexture) parent.texture(tex[2]);
            parent.vertex(1.f, -1.f, -1.f, 1, 0);
            parent.vertex(-1.f, -1.f, -1.f, 0, 0);
            parent.vertex(-1.f, 0.f, -1.f, 0, 1);
            parent.vertex(1.f, 0.f, -1.f, 1, 1);
        parent.endShape();
        parent.beginShape(PApplet.QUAD);
        // NEAR SIDE
        	if(toTexture) parent.texture(tex[3]);
            parent.vertex(-1.f, -1.f, 1.f, 1, 0);
            parent.vertex(1.f, -1.f, 1.f, 0, 0);
            parent.vertex(1.f, 0.f, 1.f, 0, 1);
            parent.vertex(-1.f, 0.f, 1.f, 1, 1);
        parent.endShape();
        parent.beginShape(PApplet.QUAD);
        // BOTTOM
        	if(toTexture) parent.texture(tex[4]);
            parent.vertex(1.f,0.f,-1.f, 1, 0);
            parent.vertex(-1.f,0.f,-1.f, 0, 0);
            parent.vertex(-1.f,0.f,1.f, 0, 1);
            parent.vertex(1.f,0.f,1.f, 1, 1);
        parent.endShape();
        parent.beginShape(PApplet.QUAD);
        // TOP
        	if(toTexture) parent.texture(tex[5]);
            parent.vertex(-1.f,-1.f,-1.f, 0, 1);
            parent.vertex(1.f,-1.f,-1.f, 1, 1);
            parent.vertex(1.f,-1.f,1.f, 1, 0);
            parent.vertex(-1.f,-1.f,1.f, 0, 0);            
        parent.endShape();
        parent.lights();
    }
	
	

}
