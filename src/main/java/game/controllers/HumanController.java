package game.controllers;

import board.Board;
import board.HexVector;
import board.Move;
import board.MoveChecker;
import game.Player;

import java.util.function.Function;

/**
 * Player controlled by human (at local computer)
 */
public class HumanController implements PlayerController {
    private Player player;
    private Board board;
    private HexVector activePawnPosition;
    private Function<Move,Boolean> actionOnMove;

    private boolean myTurn = false;

    private void deactivatePawn() {
        if(!myTurn)
            return;
        if(activePawnPosition != null) {
            board.switchPawnSelection(activePawnPosition, true);
        }
        activePawnPosition = null;
    }

    private void activatePawnAt(HexVector position) {
        if(!myTurn)
            return;
        activePawnPosition = position;
        board.switchPawnSelection(position, true);
    }

    public void onClickResponse(HexVector position) {
        if(!myTurn)
            return;
        if(player.hasPawnAt(position)) {
            if(position == activePawnPosition) {
                deactivatePawn();
            } else {
                deactivatePawn();
                activatePawnAt(position);
            }
        } else if(activePawnPosition != null) {
            HexVector prevPawnPosition = activePawnPosition;
            deactivatePawn();
            myTurn = false;
            if(!actionOnMove.apply(new Move(prevPawnPosition, position))) {
                myTurn = true;
                activatePawnAt(prevPawnPosition);
            }
        }
    }

    @Override
    public void set(Player player, Board board, MoveChecker moveChecker) {
        this.player = player;
        this.board = board;
    }

    @Override
    public Player getPlayer() {
        return player;
    }

    @Override
    public void setActionOnMove(Function<Move,Boolean> action) {
        this.actionOnMove = action;
    }

    @Override
    public void nextTurn(){
        myTurn = true;
    }

}
