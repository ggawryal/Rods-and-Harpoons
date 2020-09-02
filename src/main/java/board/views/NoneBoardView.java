package board.views;

import board.HexVector;
import board.Move;
import board.drawable.pawn.Pawn;
import board.drawable.tile.Tile;

import java.util.function.Consumer;

public class NoneBoardView implements BoardView {
    @Override
    public void drawTile(Tile tile, HexVector position) {}

    @Override
    public void drawPawn(Pawn pawn, HexVector position) {}

    @Override
    public void removeTile(HexVector position) {}

    @Override
    public void removePawn(HexVector position) {}

    @Override
    public void switchPawnSelection(HexVector position) {}

    @Override
    public void switchTileGlow(HexVector position) {}

    @Override
    public void playMoveTransition(Pawn pawn, Move move) {}

    @Override
    public void setActionOnClickForExistingTiles(Consumer<HexVector> onMouseClick) {}
}
