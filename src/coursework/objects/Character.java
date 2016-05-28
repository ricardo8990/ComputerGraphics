package coursework.objects;

import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Map.Entry;

import javax.media.opengl.*;
import javax.media.opengl.glu.*;

import processing.core.PApplet;
import processing.core.PMatrix;
import processing.core.PMatrix3D;
import processing.core.PVector;
import processing.opengl.PGL;
import processing.opengl.PGraphicsOpenGL;
import saito.objloader.OBJModel;
import framework.Input;
import framework.Scene;
import framework.objects.AnimatedObject;
import framework.objects.DisplayableObject;

public class Character extends AnimatedObject
{
	OBJModel model;
	Skeleton skeleton;
	PGraphicsOpenGL pgl;
	PGL opengl;
	GLU glu;
	
	public Character(Scene parent)
	{
		super(parent);
		model = new OBJModel(parent, Constants.model);
		skeleton = new Skeleton(Constants.skeleton, model);
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
		
		//skeleton.display(parent);
		
		parent.noStroke();
		model.enableMaterial();
		model.enableTexture();
		
		model.draw();
		
		parent.popStyle();
		parent.popMatrix();
	}

	@Override
	public void update(float dT) 
	{	
		//Get the updated bones
		ArrayList<String> updatedBones = skeleton.update(dT);
		
		if(updatedBones == null || updatedBones.size() <= 0)
			return;
		
		//Calculate the position for each vertex
		for(String boneName : updatedBones)
		{
			//Recover the bone
			Bone bone = skeleton.bones.get(boneName);
			//Get the translation matrix
			PMatrix3D transMatrix = bone.GetTransformationMatrix();
			//Get the rotation angle
			PVector rotationAngle = bone.GetRotationAngle(); 
			//Calculate the angle to rotate from the previous position
			rotationAngle = PVector.sub(rotationAngle, bone.rotationAngle);
			//Rotate the matrix
			//transMatrix.rotateX(PApplet.radians(rotationAngle.x));
			//transMatrix.rotateY(PApplet.radians(rotationAngle.y));
			transMatrix.rotateZ(PApplet.radians(rotationAngle.z));
			//Multiply by the inverse of the previous transformation matrix
			transMatrix.apply(bone.originalMatrix);
			
			//For each vertex in the bone
			for(Entry<Integer, PVector> vertex : bone.vertex.entrySet())
			{
				//Multiply the vertex by the transformation matrix
				PVector newVertex = transMatrix.mult(vertex.getValue(), null);
				vertex.setValue(newVertex);
				//Update it in the model
				model.setVertex(vertex.getKey(), vertex.getValue());
			}
			//Update the transformation matrix and the angle
			bone.originalMatrix = bone.GetTransformationMatrix();
			bone.originalMatrix.invert();
			bone.rotationAngle = bone.GetRotationAngle();
		}
		
	}

}