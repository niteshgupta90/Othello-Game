import java.util.ArrayList;
import java.util.List;

/**
 * Implements an Othello Game. It contains implementation of methods inherited
 * inherited from Game.java
 *
 * @param <S>
 *            Type which is used for states in the game.
 * @param <A>
 *            Type which is used for actions in the game.
 * @param <P>
 *            Type which is used for players in the game.
 * @author Nitesh Gupta
 */
public class OthelloGame<S, A, P> implements Game<S, A, P> {
	final int SIZE = 8;
	final int AGENT = 1;
	final int PLAYER = 2;

	int[][] state;
	int player;

	public OthelloGame() {
		state = new int[SIZE][SIZE];
		player = 0;
	}

	@Override
	public int[][] getInitialState() {
		// TODO Auto-generated method stub

		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				state[i][j] = 0;
			}
		}
		return state;
	}

	@Override
	public P[] getPlayers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPlayer() {
		// TODO Auto-generated method stub
		return this.player;
	}

	@Override
	public void setPlayer(int i) {
		this.player = i;
	}

	@Override
	public int[][] implementMove(int[][] state, int i, int j) {
		List<ValidPair> list = new ArrayList<ValidPair>();
		int[] row = new int[] { 1, -1, 0, 0, 1, 1, -1, -1 };
		int[] col = new int[] { 0, 0, 1, -1, 1, -1, -1, 1 };
		state[i][j] = this.player;
		for (int k = 0; k < 8; k++) {
			if (isValid(i + row[k], j + col[k])) {
				if (col[k] == 0)
					rowActionPlayer(i, j, row[k], state, list);
				else if (row[k] == 0)
					colActionPlayer(i, j, col[k], state, list);
				else {
					diagonalActionPlayer(i, j, row[k], col[k], state, list);
				}
			}
		}
		for (ValidPair action : list) {
			state = getResult(state, action);
		}
		return state;
	}

	void rowActionPlayer(int r, int c, int val, int[][] state, List<ValidPair> list) {
		int p = this.player == AGENT ? PLAYER : AGENT;
		int count = 0;
		ValidPair pair = new ValidPair(r, c, r, c);
		r = r + val;
		while (isValid(r, c) && state[r][c] == p) {
			r = r + val;
			count++;
		}
		if (isValid(r, c) && count > 0 && state[r][c] == this.player) {
			pair.end_x = r;
			list.add(pair);
		}
	}

	void colActionPlayer(int r, int c, int val, int[][] state, List<ValidPair> list) {
		int p = this.player == AGENT ? PLAYER : AGENT;
		int count = 0;
		ValidPair pair = new ValidPair(r, c, r, c);
		c = c + val;
		while (isValid(r, c) && state[r][c] == p) {
			c = c + val;
			count++;
		}
		if (isValid(r, c) && count > 0 && state[r][c] == this.player) {
			pair.end_y = c;
			list.add(pair);
		}
	}

	void diagonalActionPlayer(int r, int c, int val1, int val2, int[][] state, List<ValidPair> list) {
		int p = this.player == AGENT ? PLAYER : AGENT;
		int count = 0;
		ValidPair pair = new ValidPair(r, c, r, c);
		r = r + val1;
		c = c + val2;
		while (isValid(r, c) && state[r][c] == p) {
			c = c + val2;
			r = r + val1;
			count++;
		}
		if (isValid(r, c) && count > 0 && state[r][c] == this.player) {
			pair.end_x = r;
			pair.end_y = c;
			list.add(pair);
		}
	}

	@Override
	public List<ValidPair> getActions(int[][] state) {
		// TODO Auto-generated method stub
		// check row
		// check column
		// check diagonals
		List<ValidPair> list = new ArrayList<ValidPair>();
		int[] row = new int[] { 1, -1, 0, 0, 1, 1, -1, -1 };
		int[] col = new int[] { 0, 0, 1, -1, 1, -1, -1, 1 };
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				for (int k = 0; k < 8; k++) {
					if (isValid(i + row[k], j + col[k]) && state[i][j] == this.player) {
						if (col[k] == 0)
							rowAction(i, j, row[k], state, list);
						else if (row[k] == 0)
							colAction(i, j, col[k], state, list);
						else {
							diagonalAction(i, j, row[k], col[k], state, list);
						}
					}
				}
			}
		}
		return list;
	}

	boolean isValid(int x, int y) {
		return (x >= 0 && x < SIZE && y >= 0 && y < SIZE);
	}

	void rowAction(int r, int c, int val, int[][] state, List<ValidPair> list) {
		int p = this.player == AGENT ? PLAYER : AGENT;
		int count = 0;
		ValidPair pair = new ValidPair(r, c, r, c);
		r = r + val;
		while (isValid(r, c) && state[r][c] == p) {
			r = r + val;
			count++;
		}
		if (isValid(r, c) && count > 0 && state[r][c] == 0) {
			pair.end_x = r;
			list.add(pair);
		}
	}

	void colAction(int r, int c, int val, int[][] state, List<ValidPair> list) {
		int p = this.player == AGENT ? PLAYER : AGENT;
		int count = 0;
		ValidPair pair = new ValidPair(r, c, r, c);
		c = c + val;
		while (isValid(r, c) && state[r][c] == p) {
			c = c + val;
			count++;
		}
		if (isValid(r, c) && count > 0 && state[r][c] == 0) {
			pair.end_y = c;
			list.add(pair);
		}
	}

	void diagonalAction(int r, int c, int val1, int val2, int[][] state, List<ValidPair> list) {
		int p = this.player == AGENT ? PLAYER : AGENT;
		int count = 0;
		ValidPair pair = new ValidPair(r, c, r, c);
		r = r + val1;
		c = c + val2;
		while (isValid(r, c) && state[r][c] == p) {
			c = c + val2;
			r = r + val1;
			count++;
		}
		if (isValid(r, c) && count > 0 && state[r][c] == 0) {
			pair.end_x = r;
			pair.end_y = c;
			list.add(pair);
		}
	}

	@Override
	public int[][] getResult(int[][] state, ValidPair action) {
		int[][] newState = new int[state.length][state[0].length];
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				newState[i][j] = state[i][j];
			}
		}

		// TODO Auto-generated method stub

		// row operation
		if (action.start_x == action.end_x) {
			if (action.end_y > action.start_y) {
				for (int i = action.start_y + 1; i <= action.end_y; i++) {
					newState[action.start_x][i] = this.player;
				}
			}
			if (action.start_y > action.end_y) {
				for (int i = action.start_y - 1; i >= action.end_y; i--) {
					newState[action.start_x][i] = this.player;
				}
			}
		}

		// column operation
		else if (action.start_y == action.end_y) {
			if (action.end_x > action.start_x) {
				for (int i = action.start_x + 1; i <= action.end_x; i++) {
					newState[i][action.start_y] = this.player;
				}
			}
			if (action.start_x > action.end_x) {
				for (int i = action.start_x - 1; i >= action.end_x; i--) {
					newState[i][action.start_y] = this.player;
				}
			}
		}

		// diagonal operation
		else {
			int val1 = action.end_x > action.start_x ? 1 : -1;
			int val2 = action.end_y > action.start_y ? 1 : -1;
			int i = action.start_x + val1;
			int j = action.start_y + val2;

			for (; i != action.end_x && j != action.end_y; i = i + val1, j = j + val2) {
				newState[i][j] = this.player;
			}
			newState[i][j] = this.player;
		}
		return newState;
	}

	@Override
	public boolean isTerminal(int[][] state) {
		// TODO Auto-generated method stub
		int zeros = 0;
		int ones = 0;
		int twos = 0;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (state[i][j] == 0) {
					zeros++;
				}
				if (state[i][j] == 1) {
					ones++;
				}
				if (state[i][j] == 2) {
					twos++;
				}
			}
		}
		if (zeros == 0 || ones == 0 || twos == 0)
			return true;
		return false;
	}

	@Override
	public double getUtility(int[][] state, int player) {
		// TODO Auto-generated method stub
		int ones = 0;
		int twos = 0;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (state[i][j] == 1) {
					ones++;
				}
				if (state[i][j] == 2) {
					twos++;
				}
			}
		}

		return player == AGENT ? ones : twos;
	}

	@Override
	public double getImprovedUtility(int[][] state, int player) {
		// TODO Auto-generated method stub
		int ones = 0;
		int twos = 0;
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				if (state[i][j] == 1) {
					// if move at any corner position then give 10 points
					if (i == 7 && j == 7) {
						ones += 10;
					}

					// if move at any edge position then give 2 points
					else if (isEdge(i, j)) {
						ones += 2;
					}
					// otherwise give 1 point
					else {
						ones += 1;
					}
				}
				if (state[i][j] == 2) {
					// if move at any corner then give 10 points
					if (i == 7 && j == 7) {
						twos += 10;
					}
					// if move at any edge position then give 2 points
					else if (isEdge(i, j)) {
						twos += 2;
					}
					// otherwise give 1 point
					else {
						twos += 1;
					}
				}
			}
		}

		return player == AGENT ? ones : twos;
	}

	boolean isEdge(int x, int y){
		return x==0 && y==0 || x==0 && y==SIZE-1 ||  x==SIZE-1 && y==0 || x==SIZE-1 && y==SIZE-1;
	}

	@Override
	public void switchPlayer() {
		this.player = this.player == AGENT ? PLAYER : AGENT;
		// TODO Auto-generated method stub

	}

}
