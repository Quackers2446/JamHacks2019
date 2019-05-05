package kinect;

import edu.ufl.digitalworlds.j4k.DepthMap;
import edu.ufl.digitalworlds.j4k.J4KSDK;

public class Kinect extends J4KSDK {

	private float[][] depthMap;

	private boolean depthMapLoaded = false;

	public Kinect() {
		depthMap = new float[424][512];
	}

	@Override
	public void onDepthFrameEvent(short[] depth_frame, byte[] player_index, float[] XYZ, float[] UV) {
		DepthMap map = new DepthMap(512, 424, XYZ);
		int index = 0;
		for (int y = 0; y < getDepthHeight(); y++) {
			for (int x = 0; x < getDepthWidth(); x++) {
				float value = map.realZ[index];
				depthMap[y][x] = value;
				index++;
			}
		}
		depthMapLoaded = true;
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
