package board.views;

import board.HexVector;
import board.Move;
import board.drawable.pawn.Pawn;
import board.drawable.tile.Tile;

import java.util.function.Consumer;

/*main view interface*/
public interface BoardView {
    void drawTile(Tile tile, HexVector position);
    void drawPawn(Pawn pawn, HexVector position);
    void removeTile(HexVector position);
    void removePawn(HexVector position);

    void switchPawnSelection(HexVector position);
    void switchTileGlow(HexVector position);
    void playMoveTransition(Pawn pawn, Move move);

    void setActionOnClickForExistingTiles(Consumer<HexVector> onMouseClick);
}
