import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
/**
 * Main class for playing the Othello Game.
 * @author Nitesh Gupta
 */
public class Play {
	static final int SIZE = 8;
	static final int MAX = 100;
	static final int MIN = 0;
	static final int AGENT = 1;
	static final int PLAYER = 2;
	static int[][] state = {{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,1,2,0,0,0},
			{0,0,0,2,1,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0},
			{0,0,0,0,0,0,0,0}};	
	public static void main(String[] args) {
		// TODO Auto-generated method stub	
		Game<int[][], int[], Integer> game = new OthelloGame<int[][], int[], Integer>(); 
		AlphaBetaSearch<int[][], int[], Integer>  search = new AlphaBetaSearch<int[][], int[], Integer>(game, MIN, MAX);
		Scanner sc = new Scanner(System.in);
		game.setPlayer(AGENT);
		System.out.println("*********Start End********");		
		while(!game.isTerminal(state)){
			playGame(game,search,sc);
		}
		System.out.println("*********Game End********");
		sc.close();
	}
	
	static void playGame(Game<int[][], int[], Integer> game,
			AlphaBetaSearch<int[][], int[], Integer> search, Scanner sc) {
		
		List<ValidPair> list = new ArrayList<ValidPair>();		
		list = game.getActions(state);
		ValidPair pair = new ValidPair();
		if(list.isEmpty()){
			game.switchPlayer();		
			return;
		}
		if (game.getPlayer() == PLAYER) {
			System.out.println("Possible Moves are:");			
			printValidMoves(list);
			HashSet<String> set = new HashSet<String>();
			for (ValidPair p : list) {
				String str = "";				
				str+=Integer.toString(p.end_x)+Integer.toString(p.end_y);
				set.add(str);
			}
			int[] move = new int[2];
			if (isValidMove(move, set, sc))
				state = game.implementMove(state, move[0], move[1]);
			System.out.println("State After Player's Move");
		}

		else if (game.getPlayer() == AGENT) {
			pair = search.makeDecision(state);				
			state = game.getResult(state, pair);
			System.out.println("State After Agent's Move");
		}
		printBoard(state);
		game.switchPlayer();		
	}

	static boolean isValidMove(int[] move, HashSet<String> set, Scanner sc) {
		boolean isValid = false;
		while (!isValid) {
			System.out.println("Input a valid move.");
			int x = sc.nextInt();
			int y = sc.nextInt();
			String str = "";
			str+=Integer.toString(x)+Integer.toString(y);			
			if (!set.contains(str)) {
				System.out.println("Error: Invalid Move.");
			} else {
				move[0] = x;
				move[1] = y;				
				isValid = true;
			}
		}
		return isValid;
	}

	static void printValidMoves(List<ValidPair> list) {
		for (ValidPair p : list) {
			System.out.println("(" + p.end_x + ", " + p.end_y + "), ");
		}
	}

	static void printBoard(int[][] state) {
		for (int i = 0; i < SIZE; i++) {
			for (int j = 0; j < SIZE; j++) {
				System.out.print(+state[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println();		
	}
}
