package coursework.objects;

import processing.core.PApplet;
import framework.Scene;
import framework.objects.DisplayableObject;

public class Ground extends DisplayableObject
{
	private int rColor;
	private int gColor;
	private int bColor;

	public Ground(Scene parent, int rColor, int gColor, int bColor)
	{
		super(parent);
		this.rColor = rColor;
		this.gColor = gColor;
		this.bColor = bColor;
	}

	@Override
	public void display()
	{
		parent.pushMatrix();
		parent.pushStyle();
		parent.translate(pos.x, pos.y, pos.z);
		parent.scale(scale.x, scale.y, scale.z);
		parent.fill(rColor, gColor, bColor);
		parent.ambient(rColor, gColor, bColor);
		parent.specular(0, 0, 0);
		parent.rotateY(rotation.y);						// Set orientation (Y - roll)
		parent.rotateZ(rotation.z);						// Set orientation (Z - yaw)
		parent.rotateX(rotation.x);						// Set orientation (X - pitch)
		
		
		parent.noStroke();
		
		parent.beginShape(PApplet.QUAD);
		parent.normal(0.f, -1.f, 0.f);
        parent.vertex(1.f,0.f,-1.f);
        parent.vertex(-1.f,0.f,-1.f);
        parent.vertex(-1.f,0.f,0.f);
        parent.vertex(1.f,0.f,0.f);
        parent.endShape();
		
		parent.popStyle();
		parent.popMatrix();
	}

}
