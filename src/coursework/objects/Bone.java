package coursework.objects;

import java.util.HashMap;
import java.util.Map;

import processing.core.PMatrix;
import processing.core.PMatrix3D;
import processing.core.PVector;

public class Bone
{
	PVector origin;
	PVector end;
	PVector previousOrigin;
	PVector previousEnd;
	
	Bone parent;
	PMatrix3D originalMatrix;
	PVector rotationAngle;
	
	Map<Integer, PVector> vertex;
	
	Bone(PVector origin, PVector end)
	{
		this();
		this.origin = origin;
		this.previousOrigin = origin;
		this.end = end;
		this.previousEnd = end;
		this.parent = null;
		this.originalMatrix = GetTransformationMatrix();
		this.originalMatrix.invert();
		this.rotationAngle = GetRotationAngle();
	}
	
	Bone(Bone parent, PVector end)
	{
		this();
		this.origin = parent.end;
		this.previousOrigin = parent.end;
		this.end = end;
		this.previousEnd = end;
		this.parent = parent;
		this.originalMatrix = GetTransformationMatrix();
		this.originalMatrix.invert();
		this.rotationAngle = GetRotationAngle();
	}

	public Bone() 
	{	
		vertex = new HashMap<Integer, PVector>();
	}
	
	public PMatrix3D GetTransformationMatrix()
	{
		//Push matrix
		PMatrix3D matrix = new PMatrix3D();
		//Get identity matrixmatrix.print();
		matrix.reset();
		//Translate to the end position
		matrix.translate(origin.x, origin.y, origin.z);
		//Save this matrix
		return matrix;
	}
	
	public PVector GetRotationAngle()
	{
		float angleX = GetAngleTwoVectors2D(origin.z, -origin.y, end.z, -end.y);
		float angleY = GetAngleTwoVectors2D(origin.x, -origin.z, end.x, -end.z);
		float angleZ = GetAngleTwoVectors2D(origin.x, origin.y, end.x, end.y);
		return new PVector(angleX, angleY, angleZ);
	}
	
	//Return the angle range 0-360 degrees
	private float GetAngleTwoVectors2D(float x1, float y1, float x2, float y2)
	{
		float angle = (float) Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
		if(angle < 0)
			angle += 360;
		
		return angle;
	}

	public boolean updateParent()
	{
		if(parent.end.x != origin.x || parent.end.y != origin.y || parent.end.z != origin.z)
		{
			origin = parent.end;
			return true;
		}
		return false;
	}
}
