package game;

import board.Board;
import board.Move;
import board.MoveChecker;

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

    private ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Random random = new Random();


    @Override
    public void nextTurn() {
        executorService.submit(() -> {
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(200)+100); // from 100 to 300 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ArrayList<Move> moves = new ArrayList<>();
            for(int i=0;i<player.getPawnsCount();i++)
                moves.addAll(moveChecker.getPossibleMoves(player.getPawnPosition(i)));
            Move selectedMove = moves.get(random.nextInt(moves.size()));
            board.switchPawnSelection(selectedMove.getFrom(), false);
            try {
                TimeUnit.MILLISECONDS.sleep(random.nextInt(200)+300); // from 300 to 500 ms
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            boolean moved = actionOnMove.apply(selectedMove);
            if(!moved)
                throw new RuntimeException("Random moving bot selected incorrect move");
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
