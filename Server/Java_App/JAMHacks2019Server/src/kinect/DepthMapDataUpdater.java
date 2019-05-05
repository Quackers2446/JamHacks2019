package kinect;

public class DepthMapDataUpdater extends Thread {

	private Kinect kinect;
	private boolean hasNewMap = false;
	// Map sizes in pixels; will have to use trial and error to determine the best
	// values.
	private static final int MAP_SIZE_X = 1000;
	private static final int MAP_SIZE_Y = 800;
	private static final float PIXEL_SIZE_X = (float) MAP_SIZE_X / (float) 512;
	private static final float PIXEL_SIZE_Y = (float) MAP_SIZE_Y / (float) 424;
	private float[][] depthMap = new float[424][512];
	private int[][] image = new int[MAP_SIZE_Y][MAP_SIZE_X];

	public DepthMapDataUpdater(Kinect k) {
		kinect = k;
	}

	public void run() {
		if (kinect.isDepthMapLoaded()) {
			updateMapsAndImages();

		}
		sleep(10);
	}

	private void updateMapsAndImages() {
		depthMap = kinect.popDepthMap();
		int xIndex = 0;
		int yIndex = 0;
		int xOffset = 0;
		int yOffset = 0;
		for (int i = 0; i < MAP_SIZE_Y; i++) {
			xOffset = 0;
			for (int j = 0; j < MAP_SIZE_X; j++) {
				xOffset += 1;
				if (xOffset > PIXEL_SIZE_X) {
					xOffset -= PIXEL_SIZE_X;
					xIndex += 1;
				}
				image[i][j] = getColourByHeight(depthMap[yIndex][xIndex]);
			}
			yOffset += 1;
			if (yOffset > PIXEL_SIZE_Y) {
				yOffset -= PIXEL_SIZE_Y;
				yIndex += 1;
			}
		}
	}

	// The value of height is normalized.
	private int getColourByHeight(float height) {
		// TODO Auto-generated method stub
		return 0;
	}

	public boolean hasNewMap() {
		return hasNewMap;
	}

	public void setHasNewMap(boolean hasNewMap) {
		this.hasNewMap = hasNewMap;
	}

	public float[][] getDepthMap() {
		hasNewMap = false;
		return depthMap;
	}

	private void applyGaussianBlur() {

	}

	public float[][] generateWeightMatrix(int size, float variance) {
		float[][] weights = new float[size][size];
		float sum = 0;
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] = gaussianModel(i - size / 2, j - size / 2, variance);
				sum += weights[i][j];
			}
		}
		for (int i = 0; i < weights.length; i++) {
			for (int j = 0; j < weights[i].length; j++) {
				weights[i][j] /= sum;
			}

		}
		return weights;
	}

	public float gaussianModel(int x, int y, float variance) {
		return (float) (1 / (2 * Math.PI * Math.pow(variance, 2))
				* Math.exp(-(Math.pow(x, 2) + Math.pow(y, 2)) / (2 * Math.pow(variance, 2))));
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
