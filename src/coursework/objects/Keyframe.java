package coursework.objects;

import java.util.HashMap;
import java.util.Map;

public class Keyframe 
{
	public float dT;
	public Map<String, Bone> bones;
	
	public Keyframe() 
	{
		bones = new HashMap<String, Bone>();
	}
}
