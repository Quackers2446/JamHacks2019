package kinect;

import java.awt.Color;

import game.Point;

public class DepthMapDataUpdater extends Thread {

	private Kinect kinect;
	private boolean hasNewMap = false;
	// Map sizes in pixels; will have to use trial and error to determine the best
	// values.
	private static final int START_READ_PIXEL_X = 188;
	private static final int START_READ_PIXEL_Y = 31;
	private static final int NUM_READ_PIXELS_X = 248;
	private static final int NUM_READ_PIXELS_Y = 248;
	private static final int MAP_PIXEL_SIZE_X = 576;
	private static final int MAP_PIXEL_SIZE_Y = 576;
	private static final float UNIT_SIZE_X = (float) MAP_PIXEL_SIZE_X / (NUM_READ_PIXELS_X + 0.2f);
	private static final float UNIT_SIZE_Y = (float) MAP_PIXEL_SIZE_Y / (NUM_READ_PIXELS_Y + 0.2f);
	private float[][] depthMap = new float[NUM_READ_PIXELS_X][NUM_READ_PIXELS_Y];
	private float[][] heightMap = new float[MAP_PIXEL_SIZE_Y][MAP_PIXEL_SIZE_X];
	private int[][] image = new int[MAP_PIXEL_SIZE_Y][MAP_PIXEL_SIZE_X];

	private static final Color[] SEA_COLOURS = new Color[] { new Color(21, 101, 192), new Color(100, 181, 246) };
	private static final Color[] LAND_COLOURS = new Color[] { new Color(173, 255, 47), new Color(154, 205, 50),
			new Color(67, 160, 71), new Color(255, 255, 51), new Color(255, 165, 0), new Color(255, 69, 0) };
	public static final float SEA_LEVEL = 0.1f;

	private float[][] depthMapGaussianMatrix;
	private float[][] colourMapGaussianMatrix;

	public DepthMapDataUpdater(Kinect k) {
		kinect = k;
		depthMapGaussianMatrix = generateWeightMatrix(3, 1.2f);
		colourMapGaussianMatrix = generateWeightMatrix(3, 1.2f);
	}

	public void run() {
		while (true) {
			if (kinect.isDepthMapLoaded()) {
				updateMapsAndImages();
				hasNewMap = true;
			}
			sleep(10);
		}
	}

	public float getHeight(Point location) {
		try {
			return heightMap[(int) location.getY()][(int) location.getX()];
		} catch (Exception e) {
			return -1;
		}
	}

	private void updateMapsAndImages() {
		float[][] rawMap = kinect.popDepthMap();
		for (int i = 0; i < NUM_READ_PIXELS_Y; i++) {
			for (int j = 0; j < NUM_READ_PIXELS_X; j++) {
				depthMap[i][j] = rawMap[i + START_READ_PIXEL_Y][j + START_READ_PIXEL_X];
			}
		}
//		depthMap = applyGaussianBlurOnHeights(depthMap, depthMapGaussianMatrix);
		int xIndex = 0, yIndex = 0;
		int xOffset = 0, yOffset = 0;
		for (int i = 0; i < MAP_PIXEL_SIZE_Y; i++) {
			xOffset = 0;
			xIndex = 0;
			for (int j = 0; j < MAP_PIXEL_SIZE_X; j++) {
				xOffset++;
				if (xOffset > UNIT_SIZE_X) {
					xOffset -= UNIT_SIZE_X;
					xIndex++;
				}
//				System.out.println(j + " " + xIndex);
				heightMap[i][j] = depthMap[yIndex][xIndex];
				image[i][j] = getColourByHeight(depthMap[yIndex][xIndex]);
			}
			yOffset++;
			if (yOffset > UNIT_SIZE_Y) {
				yOffset -= UNIT_SIZE_Y;
				yIndex++;
			}
		}
//		image = applyGaussianBlurOnColours(image, colourMapGaussianMatrix);
	}

	// The value of height is normalized.
	private int getColourByHeight(float height) {
		float percentage;
		int r, g, b;
		Color colour2, colour1;
		try {
			if (height < SEA_LEVEL) {
				float strataSize = SEA_LEVEL / SEA_COLOURS.length - 1;
				int strata = (int) (height / strataSize);
				percentage = (height % strataSize) / strataSize;
//			System.out.println(seaLevel + " " + strata + " " + strataSize);
				colour2 = SEA_COLOURS[strata + 1];
				colour1 = SEA_COLOURS[strata];
			} else {
				float strataSize = (1 - SEA_LEVEL) / LAND_COLOURS.length - 1;
				strataSize = 0.09f;
				int strata = (int) ((height - SEA_LEVEL) / strataSize);
				percentage = ((height - SEA_LEVEL) % strataSize) / strataSize;
				colour2 = LAND_COLOURS[strata + 1];
				colour1 = LAND_COLOURS[strata];
			}
		} catch (Exception e) {
			return 0;
		}
//		System.out.println(colour1.getRed() + " " + colour2.getRed() + " " + percentage);
		r = (int) (colour1.getRed() + (colour2.getRed() - colour1.getRed()) * percentage);
		g = (int) (colour1.getGreen() + (colour2.getGreen() - colour1.getGreen()) * percentage);
		b = (int) (colour1.getBlue() + (colour2.getBlue() - colour1.getBlue()) * percentage);
		return new Color(r, g, b).getRGB();
//		return colour2.getRGB();
	}

	public boolean hasNewMap() {
		return hasNewMap;
	}

	public void resetHasNewMap() {
		this.hasNewMap = false;
	}

	public float[][] getDepthMap() {
		hasNewMap = false;
		return depthMap;
	}

	private float[][] applyGaussianBlurOnHeights(float[][] data, float[][] weights) {
		int size = weights.length;
		float[][] newData = new float[data.length][data[0].length];
		for (int i = 0; i < newData.length; i++) {
			for (int j = 0; j < newData[i].length; j++) {
				float[][] heights = new float[size][size];
				for (int y = 0; y < size; y++) {
					for (int x = 0; x < size; x++) {
						try {
							int sampleX = i + x - size / 2;
							int sampleY = i + x - size / 2;
							float currentWeight = weights[y][x];
							heights[y][x] = currentWeight * data[sampleY][sampleX];
						} catch (Exception e) {
						}
					}
				}
				newData[i][j] = getSumOfWeightedValues(heights);
			}
		}
		return newData;
	}

	private int[][] applyGaussianBlurOnColours(int[][] data, float[][] weights) {
		int size = weights.length;
		int[][] newData = new int[data.length][data[0].length];
		for (int i = 0; i < newData.length; i++) {
			for (int j = 0; j < newData[i].length; j++) {
				float[][] red = new float[size][size];
				float[][] green = new float[size][size];
				float[][] blue = new float[size][size];
				for (int y = 0; y < size; y++) {
					for (int x = 0; x < size; x++) {
						int sampleX = i + x - size / 2;
						int sampleY = i + x - size / 2;
						float currentWeight = weights[y][x];
						try {
							Color sampledColour = new Color(data[sampleY][sampleX]);
							red[y][x] = currentWeight * sampledColour.getRed();
							green[y][x] = currentWeight * sampledColour.getGreen();
							blue[y][x] = currentWeight * sampledColour.getBlue();
						} catch (Exception e) {
						}
					}
				}
				newData[i][j] = new Color((int) getSumOfWeightedValues(red), (int) getSumOfWeightedValues(green),
						(int) getSumOfWeightedValues(blue)).getRGB();
			}
		}
		return newData;
	}

	private float getSumOfWeightedValues(float[][] weightedValues) {
		float sum = 0;
		for (int i = 0; i < weightedValues.length; i++) {
			for (int j = 0; j < weightedValues[i].length; j++) {
				sum += weightedValues[i][j];
			}
		}
		return sum;
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

	public int[][] getImage() {
		return image;
	}

	private void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			System.out.println("oh no");
			e.printStackTrace();
		}
	}
}
