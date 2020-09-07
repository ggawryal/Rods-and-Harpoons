package game.controllers;

import board.Board;
import board.Move;
import board.MoveChecker;
import game.Player;
import game.controllers.strategies.BotStrategy;
import game.threads.ThreadRunner;
import util.sleeper.Sleeper;

import java.util.*;
import java.util.function.Function;

/**
 * Player controlled by AI
 */
public class BotController extends PlayerController {
    private ThreadRunner threadRunner;
    private Sleeper sleeper;
    private Random random = new Random();
    private BotStrategy strategy;

    public BotController(BotStrategy strategy) {
        this.strategy = strategy;
    }

    public BotController(BotStrategy strategy, Sleeper sleeper, ThreadRunner threadRunner) {
        this(strategy);
        setSleeper(sleeper);
        setThreadRunner(threadRunner);
    }

    public void setThreadRunner(ThreadRunner threadRunner) {
        this.threadRunner = threadRunner;
    }

    public void setSleeper(Sleeper sleeper) {
        this.sleeper = sleeper;
    }

    @Override
    public void nextTurn() {
        threadRunner.runLaterInBackground(() -> {
            sleeper.sleep(random.nextInt(250)+500); // from 500 to 750 ms
            ArrayList<Move> moves = new ArrayList<>();

            for(int i=0;i<getPlayer().getPawnsCount();i++)
                moves.addAll(getMoveChecker().getPossibleMoves(getPlayer().getPawnPosition(i)));

            Move bestValueMove = Collections.max(moves, Comparator.comparingLong(move -> strategy.getMoveValue(move)));
            moves.removeIf(move -> strategy.getMoveValue(move) < strategy.getMoveValue(bestValueMove));

            Move selectedMove = moves.get(random.nextInt(moves.size()));
            getBoard().switchPawnSelection(selectedMove.getFrom(), false);
            sleeper.sleep(random.nextInt(random.nextInt(250)+250)); // from 250 to 500 ms
            threadRunner.runLaterInMainThread(()-> getActionOnMove().apply(selectedMove));
        });
    }

    @Override
    public void set(Player player, Board board, MoveChecker moveChecker) {
        super.set(player,board,moveChecker);
        strategy.set(board,moveChecker);
    }

    @Override
    public void shutdown() {
        threadRunner.shutdown();
    }

    @Override
    public String toString() {
        if(strategy == null)
            return "Bot with no strategy";
        return "Bot with "+strategy.getClass().getSimpleName();
    }

}
