package coursework.objects;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import framework.Scene;
import framework.objects.AnimatedObject;
import processing.core.PApplet;
import processing.core.PVector;
import saito.objloader.OBJModel;

public class Skeleton
{
	Map<String, Bone> bones;
	ArrayList<String> bonesOrder;
	ArrayList<Keyframe> keyframes;
	float totalTime = 0.f;
	float initialTime = 0.f;
	
	public Skeleton(String fileName, OBJModel model)
	{
		bones = new HashMap<String, Bone>();
		bonesOrder = new ArrayList<String>();
		keyframes = new ArrayList<>();
		
		try
		{
			File xmlFile = new File(fileName);
			DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder dbBuilder = dbFactory.newDocumentBuilder();
			Document doc = dbBuilder.parse(xmlFile);
			doc.getDocumentElement().normalize();
			
			//Get the bones information
			Element bonesElement = (Element) doc.getElementsByTagName("bones").item(0);
			NodeList bonesList = bonesElement.getElementsByTagName("bone");
			for(int r = 0; r < bonesList.getLength(); r++)
			{
				Node nodeBone = bonesList.item(r);
				
				if(nodeBone.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
				Element elementBone = (Element) nodeBone;
				
				Bone bone;
				Boolean hasParent = false;
				PVector origin = null;
				PVector end = null;
				Bone parentBone = null;
				
				if(elementBone.getElementsByTagName("parent").getLength() > 0)
				{
					Element elementBoneElem = (Element) elementBone.getElementsByTagName("parent").item(0);
					hasParent = true;
					parentBone = bones.get(elementBoneElem.getAttribute("id"));
				}
				else
				{
					Element elementBoneElem = (Element) elementBone.getElementsByTagName("origin").item(0);
					hasParent = false;
					NodeList pVectors = elementBoneElem.getElementsByTagName("PVector");
					Node nodeVector = pVectors.item(0);
					Element pVector = (Element) nodeVector;
					origin = new PVector(Float.parseFloat(pVector.getAttribute("x")), Float.parseFloat(pVector.getAttribute("y")), 
							Float.parseFloat(pVector.getAttribute("z")));
				}
				
				Element elementBoneElem = (Element) elementBone.getElementsByTagName("end").item(0);
				NodeList pVectors = elementBoneElem.getElementsByTagName("PVector");
				Node nodeVector = pVectors.item(0);
				Element pVector = (Element) nodeVector;
				end = new PVector(Float.parseFloat(pVector.getAttribute("x")), Float.parseFloat(pVector.getAttribute("y")), 
						Float.parseFloat(pVector.getAttribute("z")));
				
				bone = hasParent ? new Bone(parentBone, end) : new Bone(origin, end);
				bones.put(elementBone.getAttribute("name"), bone);
				bonesOrder.add(elementBone.getAttribute("name"));
			}
			
			//Get the key frames information
			Element keyElement = (Element) doc.getElementsByTagName("keyframes").item(0);
			NodeList keyframes = keyElement.getElementsByTagName("key");
			for(int r = 0; r < keyframes.getLength(); r++)
			{
				Node nodeKey = keyframes.item(r);
				
				if(nodeKey.getNodeType() != Node.ELEMENT_NODE)
					continue;
				
				Element elementKey = (Element) nodeKey;
				
				Keyframe frame = new Keyframe();
				frame.dT = Float.parseFloat(elementKey.getAttribute("dt"));
				NodeList nodeBones = elementKey.getElementsByTagName("bone");
				for(int s = 0; s < nodeBones.getLength(); s++)
				{
					Element elementBone = (Element) nodeBones.item(s);
					Bone bone = new Bone();
					
					if(elementBone.getElementsByTagName("origin").getLength() > 0)
					{
						Element elementBoneElem = (Element) elementBone.getElementsByTagName("origin").item(0);
						Element pVector = (Element) elementBoneElem.getElementsByTagName("PVector").item(0);
						PVector origin = new PVector(Float.parseFloat(pVector.getAttribute("x")), Float.parseFloat(pVector.getAttribute("y")), 
								Float.parseFloat(pVector.getAttribute("z")));
						bone.origin = origin;
					}
					
					if(elementBone.getElementsByTagName("end").getLength() > 0)
					{
						Element elementBoneElem = (Element) elementBone.getElementsByTagName("end").item(0);
						Element pVector = (Element) elementBoneElem.getElementsByTagName("PVector").item(0);
						PVector end = new PVector(Float.parseFloat(pVector.getAttribute("x")), Float.parseFloat(pVector.getAttribute("y")), 
								Float.parseFloat(pVector.getAttribute("z")));
						bone.end = end;
					}
					
					frame.bones.put(elementBone.getAttribute("name"), bone);
				}
				
				this.keyframes.add(frame);
			}
			
		} catch (Exception e)
		{
			e.printStackTrace();
		}
		
		//Assign each vertex to the closest bone
		for(int r = 0; r < model.getVertexCount(); r++)
		{
			//Get the vertex
			PVector vertex = model.getVertex(r);
			//Calculate the closest bone
			Bone closestBone = bones.get(bonesOrder.get(0));
			float closestDistance = vertexDistanceToBone(vertex, closestBone);
			for(String boneName : bonesOrder)
			{
				Bone bone = bones.get(boneName);
				float distance = vertexDistanceToBone(vertex, bone);
				if(distance < closestDistance)
				{
					closestBone = bone;
					closestDistance = distance;
				}
			}
			//Assign to this bone
			closestBone.vertex.put(r, vertex);
		}
	}
	
	private float vertexDistanceToBone(PVector vertex, Bone bone)
	{
		PVector v = PVector.sub(bone.end, bone.origin);
		PVector w = PVector.sub(vertex, bone.origin);
		
		float c1 = PVector.dot(w, v);
		if(c1 <= 0)
			return PVector.dist(vertex, bone.origin);
		
		float c2 = PVector.dot(v, v);
		if(c2 <= c1)
			return PVector.dist(vertex, bone.end);
		
		float b = c1 / c2;
		PVector pb = PVector.add(bone.origin, PVector.mult(v, b));
		return PVector.dist(vertex, pb);
	}

	public void display(Scene parent)
	{
		parent.pushStyle();
		
		for(String boneName : bonesOrder)
		{
			Bone bone = bones.get(boneName);
			parent.pushMatrix();
			
			parent.beginShape(PApplet.LINES);
			
			parent.fill(255, 0, 0);
			parent.vertex(bone.origin.x, bone.origin.y, bone.origin.z);
			parent.fill(0, 255, 0);
			parent.vertex(bone.end.x,  bone.end.y, bone.end.z);
			
			parent.endShape();
			
			parent.popMatrix();
		}
		parent.popStyle();
	}

	public ArrayList<String> update(float dT) 
	{		
		if(keyframes.size() == 0)
			return null;
		
		ArrayList<String> updatedBones = new ArrayList<String>();
		
		totalTime += dT;
		Keyframe nextFrame = keyframes.get(0);
		
		//Interpolate each bone
		for(Entry<String, Bone> boneFrame : nextFrame.bones.entrySet())
		{
			//Get the bone
			Bone bone = bones.get(boneFrame.getKey());
			//If it has the parent update the origin to the end of its parent
			if(bone.parent != null)
			{
				bone.origin = bone.parent.end;
			}
			//Otherwise interpolate the origin if it has been specified
			else if(boneFrame.getValue().origin != null)
			{
				bone.origin = interpolateVectors(bone.previousOrigin, boneFrame.getValue().origin, totalTime, nextFrame.dT);
			}
			//Interpolate the end
			if(boneFrame.getValue().end != null)
			{
				bone.end = interpolateVectors(bone.previousEnd, boneFrame.getValue().end, totalTime, nextFrame.dT);
			}
			
			//Add it to the list of updated bones
			updatedBones.add(boneFrame.getKey());
		}
		
		//Update each bone according to its parent
		for(String boneName : bonesOrder)
		{
			Bone bone = bones.get(boneName);
			if(bone.parent == null)
				continue;
			if(bone.updateParent() && !updatedBones.contains(boneName))
				updatedBones.add(boneName);
		}
		
		if(totalTime < nextFrame.dT)
			return updatedBones;
		//When the time is done
		//Update the previous position for each bone
		for(Entry<String, Bone> boneFrame : nextFrame.bones.entrySet())
		{
			Bone bone = bones.get(boneFrame.getKey());
			bone.previousOrigin = bone.origin;
			bone.previousEnd = bone.end;
		}
		initialTime = totalTime;
		
		//Remove the frame
		keyframes.remove(nextFrame);
		
		return updatedBones;
	}

	private PVector interpolateVectors(PVector previous, PVector end, float dT, float finaldT) 
	{
		PVector vector = new PVector();
		//Interpolation for x value
		vector.x = interpolate(previous.x, end.x, dT, finaldT);
		//Interpolation for y value
		vector.y = interpolate(previous.y, end.y, dT, finaldT);
		//Interpolation for z value
		vector.z = interpolate(previous.z, end.z, dT, finaldT);
		
		return vector;
	}

	private float interpolate(float y0, float y, float x, float x1) 
	{
		return y0 + ((y - y0) * ((x - initialTime) / (x1 - initialTime))); 
	}
}
