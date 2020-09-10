package game;

import board.Board;
import board.views.BoardView;
import board.HexVector;
import board.arranger.RatioTileChooser;
import board.arranger.RectangleTileArranger;
import board.arranger.TileArranger;
import board.arranger.TileScoreChooser;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;


class PawnSetUpperTest {
    @Test
    void testOnly1Score() {
        PawnSetUpper pawnSetUpper = new PawnSetUpper();
        BoardView boardView = mock(BoardView.class);
        Board board = new Board(boardView, 8);
        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,2,1);
        TileArranger tileArranger = new RectangleTileArranger(board.getSize(),board.getSize());
        tileArranger.arrange(board,tileScoreChooser);

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(0, "player1"));
        players.add(new Player(1, "player2"));
        players.add(new Player(2, "player3"));

        pawnSetUpper.setUpPawns(board,players,3);

        for(Player player : players) {
            for(HexVector hexVector : board.getPawnsPositions(player.getId())) {
                assertEquals(board.getTileAt(hexVector).getScore(),1);
            }
        }
    }

    @Test
    void testNotEnoughTiles() {
        PawnSetUpper pawnSetUpper = new PawnSetUpper();
        BoardView boardView = mock(BoardView.class);
        Board board = new Board(boardView, 4);
        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,2,1);
        TileArranger tileArranger = new RectangleTileArranger(board.getSize(),board.getSize());
        tileArranger.arrange(board,tileScoreChooser);

        ArrayList<Player> players = new ArrayList<>();
        players.add(new Player(0, "player1"));
        players.add(new Player(1, "player2"));
        players.add(new Player(2, "player3"));

        assertThrows(RuntimeException.class, () -> pawnSetUpper.setUpPawns(board,players,3));
    }
}
