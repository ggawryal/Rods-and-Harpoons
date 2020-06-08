package game.controllers;

import board.Board;
import board.Move;
import board.MoveChecker;
import game.Player;
import game.controllers.strategies.BotStrategy;
import game.threads.ThreadRunner;
import util.Sleeper;

import java.util.*;
import java.util.function.Function;

public class BotController implements PlayerController {
    private Player player;
    private Board board;
    private MoveChecker moveChecker;
    private Function<Move,Boolean> actionOnMove;

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
            sleeper.sleep(random.nextInt(200)+100); // from 100 to 300 ms
            ArrayList<Move> moves = new ArrayList<>();

            for(int i=0;i<player.getPawnsCount();i++)
                moves.addAll(moveChecker.getPossibleMoves(player.getPawnPosition(i)));

            Move bestValueMove = Collections.max(moves, Comparator.comparingLong(move -> strategy.getMoveValue(move)));
            moves.removeIf(move -> strategy.getMoveValue(move) < strategy.getMoveValue(bestValueMove));

            Move selectedMove = moves.get(random.nextInt(moves.size()));
            board.switchPawnSelection(selectedMove.getFrom(), false);
            sleeper.sleep(random.nextInt(random.nextInt(200)+300)); // from 300 to 500 ms
            threadRunner.runLaterInMainThread(()-> actionOnMove.apply(selectedMove));
        });
    }

    @Override
    public void set(Player player, Board board, MoveChecker moveChecker) {
        this.player = player;
        this.board = board;
        this.moveChecker = moveChecker;
        strategy.set(board,moveChecker);
    }

    @Override
    public void setActionOnMove(Function<Move, Boolean> action) {
        this.actionOnMove = action;
    }

    @Override
    public String toString() {
        if(strategy == null)
            return "Bot with no strategy";
        return "Bot with "+strategy.getClass().getSimpleName();
    }

}
