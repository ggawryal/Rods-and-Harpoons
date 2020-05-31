package game;

import board.Board;
import board.Move;

import java.util.function.Consumer;
import java.util.function.Function;

public interface PlayerController {
    void setPlayerAndBoard(Player player, Board board);
    void setActionOnMove(Function<Move,Boolean> action);
}
