package kinect;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

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

	private final Color[] SEA_COLOURS = new Color[] { new Color(21, 101, 192), new Color(100, 181, 246) };
	private final Color[] LAND_COLOURS = new Color[] { new Color(67, 160, 71), new Color(255, 238, 88),
			new Color(244, 67, 54) };
	private final float seaLevel = 0.3f;

	private float[][] depthMapGaussianMatrix;
	private float[][] colourMapGaussianMatrix;

	public DepthMapDataUpdater(Kinect k) {
		kinect = k;
		depthMapGaussianMatrix = generateWeightMatrix(5, 2);
		colourMapGaussianMatrix = generateWeightMatrix(9, 2);
	}

	public void run() {
		if (kinect.isDepthMapLoaded()) {
//			System.out.println("hi");
			updateMapsAndImages();
			System.out.println("loaded");
			hasNewMap = true;
		}
		sleep(10);
	}

	private void updateMapsAndImages() {
		depthMap = kinect.popDepthMap();
		depthMap = applyGaussianBlurOnHeights(depthMap, depthMapGaussianMatrix);
		int xIndex = 0, yIndex = 0;
		int xOffset = 0, yOffset = 0;
		for (int i = 0; i < MAP_SIZE_Y; i++) {
			xOffset = 0;
			for (int j = 0; j < MAP_SIZE_X; j++) {
				xOffset++;
				if (xOffset > PIXEL_SIZE_X) {
					xOffset -= PIXEL_SIZE_X;
					xIndex++;
				}
				image[i][j] = getColourByHeight(depthMap[yIndex][xIndex]);
			}
			yOffset++;
			if (yOffset > PIXEL_SIZE_Y) {
				yOffset -= PIXEL_SIZE_Y;
				yIndex++;
			}
		}
		image = applyGaussianBlurOnColours(image, colourMapGaussianMatrix);
		createImage();
	}

	private void createImage() {
		BufferedImage img = new BufferedImage(MAP_SIZE_X, MAP_SIZE_Y, BufferedImage.TYPE_INT_RGB);
		for (int i = 0; i < image.length; i++) {
			for (int j = 0; j < image[i].length; j++) {
				img.setRGB(i, j, image[i][j]);
			}
		}
		File outputFile = new File("res/depthImage.png");
		try {
			ImageIO.write(img, "png", outputFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// The value of height is normalized.
	private int getColourByHeight(float height) {
		float percentage;
		float r, g, b;
		Color colour2, colour1;

		if (height < seaLevel) {
			float strataSize = seaLevel / SEA_COLOURS.length;
			int strata = (int) (height / strataSize);
			percentage = height % strataSize;
			colour2 = SEA_COLOURS[strata + 1];
			colour1 = SEA_COLOURS[strata];
		} else {
			float strataSize = (1 - seaLevel) / LAND_COLOURS.length;
			int strata = (int) ((height - seaLevel) / strataSize);
			percentage = (height - seaLevel) % strataSize;
			colour2 = LAND_COLOURS[strata + 1];
			colour1 = LAND_COLOURS[strata];
		}

		r = colour1.getRed() + (colour2.getRed() - colour1.getRed()) * percentage;
		g = colour1.getGreen() + (colour2.getGreen() - colour1.getGreen()) * percentage;
		b = colour1.getBlue() + (colour2.getBlue() - colour1.getBlue()) * percentage;

		return new Color(r, g, b).getRGB();
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
							System.out.println("out of bounds");
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
						Color sampledColour = new Color(data[sampleY][sampleX]);
						red[y][x] = currentWeight * sampledColour.getRed();
						green[y][x] = currentWeight * sampledColour.getGreen();
						blue[y][x] = currentWeight * sampledColour.getBlue();
					}
				}
				newData[i][j] = new Color(getSumOfWeightedValues(red), getSumOfWeightedValues(green),
						getSumOfWeightedValues(blue)).getRGB();
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
			e.printStackTrace();
		}
	}
}
