package game;

import board.Board;
import board.Move;
import board.MoveChecker;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
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
            ArrayList<Move> moves = new ArrayList<>();
            for(int i=0;i<player.getPawnsCount();i++)
                moves.addAll(moveChecker.getPossibleMoves(player.getPawnPosition(i)));
            Move selectedMove = moves.get(random.nextInt(moves.size()));
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
