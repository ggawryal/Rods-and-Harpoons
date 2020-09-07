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
public class HumanController extends PlayerController {
    private HexVector activePawnPosition;

    private boolean myTurn = false;

    private void deactivatePawn() {
        if(!myTurn)
            return;
        if(activePawnPosition != null) {
            getBoard().switchPawnSelection(activePawnPosition, true);
        }
        activePawnPosition = null;
    }

    private void activatePawnAt(HexVector position) {
        if(!myTurn)
            return;
        activePawnPosition = position;
        getBoard().switchPawnSelection(position, true);
    }

    public void onClickResponse(HexVector position) {
        if(!myTurn)
            return;
        if(getPlayer().hasPawnAt(position)) {
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
            if(!getActionOnMove().apply(new Move(prevPawnPosition, position))) {
                myTurn = true;
                activatePawnAt(prevPawnPosition);
            }
        }
    }

    @Override
    public void nextTurn(){
        myTurn = true;
    }

}
