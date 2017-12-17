
import java.util.ArrayList;
import java.util.List;

/**
 * Implements an iterative deepening Minimax search with alpha-beta pruning and
 * action ordering. Maximal computation time is specified in seconds. The
 * algorithm is implemented as template method and can be configured and tuned
 * by subclassing.
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
public class AlphaBetaSearch<S, A, P> implements AdversarialSearch<S, A> {

	public final static String METRICS_NODES_EXPANDED = "nodesExpanded";
	public final static String METRICS_MAX_DEPTH = "maxDepth";

	protected Game<S, A, P> game;
	protected double utilMax;
	protected double utilMin;
	protected int currDepthLimit;
	
    private Metrics metrics = new Metrics();

	/**
	 * Creates a new search object for a given game.
	 *
	 * @param game
	 *            The game.
	 * @param utilMin
	 *            Utility value of worst state for this player. Supports
	 *            evaluation of non-terminal states.
	 * @param utilMax
	 *            Utility value of best state for this player. Supports
	 *            evaluation of non-terminal states.
	 */
	public AlphaBetaSearch(Game<S, A, P> game, double utilMin, double utilMax) {
		this.game = game;
		this.utilMin = utilMin;
		this.utilMax = utilMax;
	}

	/**
	 * Template method controlling the search. It is based on iterative
	 * deepening and tries to make to a good decision in limited time. Credit
	 * goes to Behi Monsio who had the idea of ordering actions by utility in
	 * subsequent depth-limited search runs.
	 */

	@Override
	public ValidPair makeDecision(int[][] state) {
        metrics = new Metrics();
		int player = game.getPlayer();
		List<ValidPair> results = orderActions(state, game.getActions(state), player, 0);
		currDepthLimit = 0;
		incrementDepthLimit();
		ActionStore<ValidPair> newResults = new ActionStore<>();
		for (ValidPair action : results) {
			double value = minValue(game.getResult(state, action), player, Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY, 1);
			newResults.add(action, value);
		}
		if (newResults.size() > 0) {
			results = newResults.actions;
		}
		return results.get(0);
	}

	// make decision without taking depth into account
	public ValidPair makeDecisionWithoutDepth(int[][] state) {
		ValidPair result = null;
		double resultValue = Double.NEGATIVE_INFINITY;
		int player = game.getPlayer();
		for (ValidPair action : game.getActions(state)) {
			double value = minValue(game.getResult(state, action), player, Double.NEGATIVE_INFINITY,
					Double.POSITIVE_INFINITY, Integer.MAX_VALUE);
			if (value > resultValue) {
				result = action;
				resultValue = value;
			}
		}
		return result;
	}

	// returns an utility value
	public double maxValue(int[][] is, int player, double alpha, double beta, int depth) {
        updateMetrics(depth);
		if (game.isTerminal(is) || depth >= currDepthLimit) {
			return eval(is, player);
		} else {
			double value = Double.NEGATIVE_INFINITY;
			for (ValidPair action : orderActions(is, game.getActions(is), player, depth)) {
				value = Math.max(value, minValue(game.getResult(is, action), //
						player, alpha, beta, depth + 1));
				if (value >= beta)
					return value;
				alpha = Math.max(alpha, value);
			}
			return value;
		}
	}

	// returns an utility value
	public double minValue(int[][] state, int player, double alpha, double beta, int depth) {
        updateMetrics(depth);
		if (game.isTerminal(state) || depth >= currDepthLimit) {
			return eval(state, player);
		} else {
			double value = Double.POSITIVE_INFINITY;
			for (ValidPair action : orderActions(state, game.getActions(state), player, depth)) {
				value = Math.min(value, maxValue(game.getResult(state, action), //
						player, alpha, beta, depth + 1));
				if (value <= alpha)
					return value;
				beta = Math.min(beta, value);
			}
			return value;
		}
	}

    private void updateMetrics(int depth) {
        metrics.incrementInt(METRICS_NODES_EXPANDED);
        metrics.set(METRICS_MAX_DEPTH, Math.max(metrics.getInt(METRICS_MAX_DEPTH), depth));
    }

    /**
     * Returns some statistic data from the last search.
     */
    @Override
    public Metrics getMetrics() {
        return metrics;
    }
    
	/**
	 * Primitive operation which is called at the beginning of one depth limited
	 * search step. This implementation increments the current depth limit by
	 * one.
	 */
	protected void incrementDepthLimit() {
		currDepthLimit++;
	}

	/**
	 * Primitive operation which is used to stop iterative deepening search in
	 * situations where a safe winner has been identified. This implementation
	 * returns true if the given value (for the currently preferred action
	 * result) is the highest or lowest utility value possible.
	 */
	protected boolean hasSafeWinner(double resultUtility) {
		return resultUtility <= utilMin || resultUtility >= utilMax;
	}

	/**
	 * Primitive operation, which estimates the value for (not necessarily
	 * terminal) states. This implementation returns the utility value for
	 * terminal states and <code>(utilMin + utilMax) / 2</code> for non-terminal
	 * states. When overriding, first call the super implementation!
	 */
	protected double eval(int[][] is, int player) {
		if (game.isTerminal(is)) {
			return game.getImprovedUtility(is, player);
		} else {
			return (utilMin + utilMax) / 2;
		}
	}

	/**
	 * Primitive operation for action ordering. This implementation preserves
	 * the original order (provided by the game).
	 */
	public List<ValidPair> orderActions(int[][] state, List<ValidPair> list, int player, int depth) {
		return list;
	}

	/**
	 * Orders actions by utility.
	 */
	private static class ActionStore<A> {
		private List<ValidPair> actions = new ArrayList<ValidPair>();
		private List<Double> utilValues = new ArrayList<>();

		void add(ValidPair action, double utilValue) {
			int idx = 0;
			while (idx < actions.size() && utilValue <= utilValues.get(idx))
				idx++;
			actions.add(idx, action);
			utilValues.add(idx, utilValue);
		}

		int size() {
			return actions.size();
		}
	}
}
