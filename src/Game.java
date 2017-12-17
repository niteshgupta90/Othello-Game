
import java.util.List;

/**
 *
 * @param <S>
 *            Type which is used for states in the game.
 * @param <A>
 *            Type which is used for actions in the game.
 * @param <P>
 *            Type which is used for players in the game.
 * @author Ruediger Lunde
 * @Edited Nitesh Gupta
 */
public interface Game<S, A, P> {

	int[][] getInitialState();

	P[] getPlayers();

	int getPlayer();

	void setPlayer(int i);

	void switchPlayer();

	public List<ValidPair> getActions(int[][] state);

	int[][] getResult(int[][] state, ValidPair action);

	boolean isTerminal(int[][] is);

	double getUtility(int[][] state, int player);

	double getImprovedUtility(int[][] state, int player);

	int[][] implementMove(int[][] state, int i, int j);

}