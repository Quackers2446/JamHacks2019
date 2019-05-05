package kinect;

import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.J4KSDK;

public class Kinect extends J4KSDK {

	private float[][] depthMap = null;
	private boolean depthMapLoaded = false;

	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] player_index, float[] XYZ, float[] UV) {
		DepthMap map = new DepthMap(512, 424, XYZ);
		float[][] tempDepthMap = new float[424][512];
		int index = 0;
		for (int y = 0; y < getDepthHeight(); y++) {
			for (int x = 0; x < getDepthWidth(); x++) {
//				float value = 1.0f - (map.realZ[index]) / MAX_HEIGHT;
				float value = 1.0f - (map.realZ[index] - 0.50f) / 0.14f; // Change this equation to adjust readings.
				tempDepthMap[y][x] = value;
				index++;
			}
		}
		depthMap = tempDepthMap;
		depthMapLoaded = true;
	}

	public float[][] popDepthMap() {
		float[][] returnMap = depthMap;
		depthMap = null;
		depthMapLoaded = false;
		return returnMap;
	}

	@Override
	public void onColorFrameEvent(byte[] arg0) {
		System.out.println("Color frame event");
	}

	@Override
	public void onSkeletonFrameEvent(boolean[] arg0, float[] arg1, float[] arg2, byte[] arg3) {
		System.out.println("Skeleton frame event");
	}

	public float[][] getDepthMap() {
		return depthMap;
	}

	public boolean isDepthMapLoaded() {
		return depthMapLoaded;
	}

}
