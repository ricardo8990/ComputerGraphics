package coursework.objects;

import processing.core.PVector;
import framework.Camera;
import framework.Scene;

public class CustomCamera extends Camera
{
	private float resolution = 15f;

	public CustomCamera(Scene parent)
	{
		super(parent);
	}
	
	@Override
	public void handleSpecialKey(int keyCode, int state, int mX, int mY)
	{
		switch (keyCode)
		{
		case LEFT:
			super.view.add(PVector.mult(super.right, -resolution));
			break;
			
		case RIGHT:
			super.view.add(PVector.mult(super.right, resolution));
			break;
			
		case UP:
			super.eye.add(PVector.mult(forward, resolution));
			super.view.add(PVector.mult(forward, resolution));
			break;
			
		case DOWN:
			super.eye.add(PVector.mult(forward, -resolution));
			super.view.add(PVector.mult(forward, -resolution));
			break;
		}
		calculateVectors();
	}
	
	@Override
	public void handleKey(char key, int state, int mX, int mY)
	{
		super.handleKey(key, state, mX, mY);
		if(key == '1')
			Constants.useOrthogonalProjection = true;
		else if(key == '2')
			Constants.useOrthogonalProjection = false;
	}

}
