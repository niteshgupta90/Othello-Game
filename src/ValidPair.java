/**
 * Represents valid coordinates(moves) for a particular start coordinate.
 * 
 * @author Nitesh Gupta
 */
public class ValidPair {
	int start_x;
	int start_y;
	int end_x;
	int end_y;

	ValidPair() {
		this.start_x = 0;
		this.start_y = 0;
		this.end_x = 0;
		this.end_y = 0;
	}

	ValidPair(int start_x, int start_y, int end_x, int end_y) {
		this.start_x = start_x;
		this.start_y = start_y;
		this.end_x = end_x;
		this.end_y = end_y;
	}
}
