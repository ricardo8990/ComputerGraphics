package coursework.objects;

public class Constants
{
	/**
	 * Palm tree
	 */
	public static int numberOfPalmTrees = 40;
	public static float palmSize = 200.f;
	public static float startingXValue = 800.f;
	public static float endXValue = 1600.f;
	public static float startingYValue = 80.f;
	public static float endYValue = -800.f;
	public static int NUMBER_OF_LEAVES = 19;
	public static float TRUNK_RADIUS = 0.01f;
	public static float TRUNK_WIDTH = 2f;
	public static float LEAVE_RADIUS = 0.7f;
	public static float trunkColour[] = {149, 69, 53};
	public static float leaveColour[] = {12, 149, 10};
	
	/**
	 * Character
	 */
	public static float characterSize = 16.f;
	public static float charXPos = 0.f;
	public static float charYPos = -90.f;
	public static float charZPos = -10.f;
	
	/**
	 * Stage
	 */
	public static float stageSize = 1000.f;
	
	/**
	 * Projection
	 */
	public static Boolean useOrthogonalProjection = true;
	
	/**
	 * Character
	 */
	public static String model = "Data/chavo.obj";
	public static String skeleton = "Data/animation.xml";
	
	/**
	 * Filename routes
	 */
	public static String TextureBark = "Textures/palm_tree_bark.jpg";
	public static String TextureLeave = "Textures/leave_palm_tree.jpg";
	public static String SkyboxLeft = "Textures/skybox_left.png";
	public static String SkyboxRight = "Textures/skybox_right.png";
	public static String SkyboxFront = "Textures/skybox_front.png";
	public static String SkyboxBack = "Textures/skybox_back.png";
	public static String SkyboxDown = "Textures/skybox_down.png";
	public static String SkyboxUp = "Textures/skybox_up.png";
	
	/**
	 * Ligthing
	 */
	public static float ambience[] = {51.f, 51.f, 51.f};
	public static float diffuse[] = {128.f, 128.f, 128.f};
	public static float spec_col[] = {255.f, 255f, 255.f};
	public static float direction[] = {-1.f, 0.5f, -1.f, 0.f};
	
	/**
	 * Music
	 */
	public static String fileMusic = "Data/LoHacesBueno.wav";
}
