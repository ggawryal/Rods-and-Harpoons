package game.controllers;

import board.Board;
import board.Move;
import board.MoveChecker;
import game.Player;

import java.util.function.Function;

public interface PlayerController {
    void set(Player player, Board board, MoveChecker moveChecker);
    Player getPlayer();
    void setActionOnMove(Function<Move,Boolean> action);
    void nextTurn();
}
