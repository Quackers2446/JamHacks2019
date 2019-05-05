package game;

public class Point {
	float x, y;

	public Point(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public static Point midpoint(Point point1, Point point2) {
		return new Point((point1.x + point2.x) / 2, (point1.y + point2.y) / 2);
	}

	public static float getDistanceSquared(Point point1, Point point2) {
		// This method is faster because you don't need to call the square root
		// function.
		return (float) (Math.pow(point1.x - point2.x, 2) + Math.pow(point1.y - point2.y, 2));
	}

	public static float getDistance(Point point1, Point point2) {
		// This method is slower because you do need to call the square root
		// function.
		return (float) Math.sqrt(getDistanceSquared(point1, point2));
	}

	public static Point getPointByAngleAndOffset(Point source, float angle, float distance) {
		// angle is from positive x-axis like in normal math, but going clockwise,
		// unlike normal math. The positive y-axis points downwards, unlike in normal
		// math.
		return new Point(source.x + (float) Math.cos(angle), source.y - (float) Math.asin(angle));
	}

}
