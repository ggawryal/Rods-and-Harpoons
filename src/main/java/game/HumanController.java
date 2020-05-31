package game;

import board.Board;
import board.HexVector;
import board.Move;

import java.util.function.Consumer;
import java.util.function.Function;

public class HumanController implements PlayerController {
    private Player player;
    private Board board;
    private HexVector activePawnPosition;

    private Function<Move,Boolean> actionOnMove;

    private void deactivatePawn() {
        if(activePawnPosition != null) {
            board.switchPawnSelection(activePawnPosition);
        }
        activePawnPosition = null;
    }

    private void activatePawnAt(HexVector position) {
        activePawnPosition = position;
        board.switchPawnSelection(position);
    }

    public void onClickResponse(HexVector position) {
        if(player.hasPawnAt(position)) {
            deactivatePawn();
            activatePawnAt(position);
        } else if(activePawnPosition != null) {
            HexVector prevPawnPosition = activePawnPosition;
            deactivatePawn();
            if(!actionOnMove.apply(new Move(prevPawnPosition, position))) {
                activatePawnAt(prevPawnPosition);
            }
        }
    }

    @Override
    public void setPlayerAndBoard(Player player,Board board) {
        this.player = player;
        this.board = board;
    }

    @Override
    public void setActionOnMove(Function<Move,Boolean> action) {
        this.actionOnMove = action;
    }
}
