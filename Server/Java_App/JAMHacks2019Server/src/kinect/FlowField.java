package kinect;

public class FlowField {
	float[][] field = null;
	
	public FlowField() {
		
	}
	
	private float neighbours(int x, int y) {
		float out[] = new float[8];
		float min = field[x][y];
		int minNum = -1;
		
		for(int i = 0; i < 8; i++) {
			try {
				if(i == 0) {
					out[i] = (field[x-1][y-1]);
				} else if (i == 1) {
					out[i] = (field[x][y-1]);
				} else if (i == 2) {
					out[i] = (field[x+1][y-1]);
				} else if (i == 3) {
					out[i] = (field[x-1][y]);
				} else if (i == 4) {
					out[i] = (field[x+1][y]);
				} else if (i == 5) {
					out[i] = (field[x-1][y+1]);
				} else if (i == 6) {
					out[i] = (field[x][y+1]);
				} else if (i == 7) {
					out[i] = (field[x+1][y+1]);
				}
			} catch(Exception e) {
				out[i] = 1f;
			} finally {
				if (out[i] < min) {
					min = out[i];
					minNum = i;
				}
			}
		}
		if (minNum == -1) {
			return 0f;
		} else {
			return 45*(minNum-1)%360;
		}
	}
	
	private float[][] generateField() {
		float out[][] = new float[field.length][field[0].length];
		for(int i = 0; i < field.length; i++) {
			for(int j = 0; j < field[0].length; j++) {
				out[j][i] = neighbours(j, i);
			} 
		}
		return out;
	}
	
	public float[][] update(float[][] array){
		field = array;
		return generateField();
	}
}
