package game.controllers;

import board.Board;
import board.Move;
import board.MoveChecker;
import game.Player;
import game.controllers.PlayerController;
import game.threads.ThreadRunner;
import util.Sleeper;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

public class RandomMovingBot implements PlayerController {
    private Player player;
    private Board board;
    private MoveChecker moveChecker;
    private Function<Move,Boolean> actionOnMove;

    private ThreadRunner threadRunner;
    Sleeper sleeper;
    private Random random = new Random();

    public RandomMovingBot(ThreadRunner threadRunner, Sleeper sleeper) {
        this.threadRunner = threadRunner;
        this.sleeper = sleeper;
    }

    @Override
    public void nextTurn() {
        threadRunner.runLaterInBackground(() -> {
            sleeper.sleep(random.nextInt(200)+100); // from 100 to 300 ms
            ArrayList<Move> moves = new ArrayList<>();
            for(int i=0;i<player.getPawnsCount();i++)
                moves.addAll(moveChecker.getPossibleMoves(player.getPawnPosition(i)));
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
    }

    @Override
    public void setActionOnMove(Function<Move, Boolean> action) {
        this.actionOnMove = action;
    }


}
