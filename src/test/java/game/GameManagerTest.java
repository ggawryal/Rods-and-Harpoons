package game;

import board.*;
import board.arranger.*;
import board.drawable.pawn.Pawn;
import board.drawable.tile.ScoreTile;
import board.views.BoardView;
import game.controllers.*;
import game.controllers.botcontrollerfactories.EasyBotControllerFactory;
import game.threads.OnlyMainThreadRunner;
import org.junit.jupiter.api.Test;
import util.sleeper.FakeSleeper;

import java.util.ArrayList;

import static application.Properties.DEFAULT_BOARD_SIZE;
import static application.Properties.DEFAULT_PAWNS_PER_PLAYER;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GameManagerTest {
    @Test
    void testInit() {
        MoveChecker moveChecker = mock(MoveChecker.class);
        Board board = new Board(mock(BoardView.class), 8);
        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,2,1);
        TileArranger tileArranger = new RectangleTileArranger(board.getSize(),board.getSize());
        tileArranger.arrange(board,tileScoreChooser);
        ArrayList<String> nicknames = new ArrayList<>();
        ArrayList<ControllerFactory> controllers = new ArrayList<>();
        nicknames.add("player");

        //more nicknames than controllers
        assertThrows(RuntimeException.class, () -> new GameManager(moveChecker, board, nicknames, controllers, 2));

        nicknames.clear();
        GameManager gameManager = new GameManager(moveChecker, board, nicknames, controllers, 2);

        assertEquals(0, gameManager.getPlayersNumber());
        assertEquals(0, board.getNumOfPawns());
    }

    @Test
    void testGetAndAddPlayersAndControllers() {
        MoveChecker moveChecker = mock(MoveChecker.class);
        Board board = new Board(mock(BoardView.class), 8);
        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,2,1);
        TileArranger tileArranger = new RectangleTileArranger(board.getSize(),board.getSize());
        tileArranger.arrange(board,tileScoreChooser);
        ArrayList<String> nicknames = new ArrayList<>();
        ArrayList<ControllerFactory> controllers = new ArrayList<>();
        for(int i=0; i<3; i++) {
            nicknames.add("player"+i);
            controllers.add(i%2==0 ? new HumanControllerFactory() : new EasyBotControllerFactory(new FakeSleeper(), new OnlyMainThreadRunner()));
        }

        GameManager gameManager = new GameManager(moveChecker, board, nicknames, controllers, 2);

        assertEquals(3, gameManager.getPlayers().size());
        for(int i=0; i<3; i++) {
            assertEquals("player"+i, gameManager.getPlayers().get(i).getNickname());
            assertEquals("player"+i, gameManager.getControllers().get(i).getPlayer().getNickname());
        }
        assertEquals(6, board.getNumOfPawns());
    }

    @Test
    void testTryToMove() {
        Board board = new Board(mock(BoardView.class), 8);
        MoveChecker moveChecker = new StandardMoveChecker(board);
        TileScoreChooser tileScoreChooser = new RatioTileChooser(3,2,1);
        TileArranger tileArranger = new RectangleTileArranger(board.getSize(),board.getSize());
        tileArranger.arrange(board,tileScoreChooser);
        ArrayList<String> nicknames = new ArrayList<>();
        ArrayList<ControllerFactory> controllers = new ArrayList<>();
        for(int i=0; i<2; i++) {
            nicknames.add("player"+i);
            controllers.add(getFactoryOfMockedController());
        }
        GameManager gameManager = new GameManager(moveChecker, board, nicknames, controllers, 0);
        gameManager.setObserver(mock(GameObserver.class));

        Pawn pawn1 = new Pawn(1);
        Pawn pawn2 = new Pawn(2);
        board.addPawn(pawn1, new HexVector(0,0));
        gameManager.getPlayers().get(0).addPawn(new HexVector(0,0));
        board.addPawn(pawn2, new HexVector(0,1));
        gameManager.getPlayers().get(1).addPawn(new HexVector(0,1));

        //wrong move
        Move move = new Move(new HexVector(0,0), new HexVector(1,1));
        assertFalse(gameManager.tryToMove(gameManager.getPlayers().get(0), move));
        //wrong player
        move = new Move(new HexVector(0,1), new HexVector(1,1));
        assertFalse(gameManager.tryToMove(gameManager.getPlayers().get(1), move));
        //correct
        move = new Move(new HexVector(0,0), new HexVector(1,0));
        assertTrue(gameManager.tryToMove(gameManager.getPlayers().get(0), move));
        //wrong player
        move = new Move(new HexVector(1,0), new HexVector(2,0));
        assertFalse(gameManager.tryToMove(gameManager.getPlayers().get(0), move));
        //correct
        move = new Move(new HexVector(0,1), new HexVector(1,1));
        assertTrue(gameManager.tryToMove(gameManager.getPlayers().get(1), move));
    }

    @Test
    void testPointUpdating() {
        Board board = new Board(mock(BoardView.class), 8);
        MoveChecker moveChecker = new StandardMoveChecker(board);
        TileScoreChooser tileScoreChooser = new SameScoreTileChooser();
        TileArranger tileArranger = new RectangleTileArranger(board.getSize(),board.getSize());
        tileArranger.arrange(board,tileScoreChooser);
        ArrayList<String> nicknames = new ArrayList<>();
        ArrayList<ControllerFactory> controllers = new ArrayList<>();
        for(int i=0; i<2; i++) {
            nicknames.add("player"+i);
            controllers.add(getFactoryOfMockedController());
        }
        GameManager gameManager = new GameManager(moveChecker, board, nicknames, controllers, 0);
        GameObserver gameObserver = mock(GameObserver.class);
        gameManager.setObserver(gameObserver);

        Pawn pawn1 = new Pawn(1);
        Pawn pawn2 = new Pawn(2);
        board.addPawn(pawn1, new HexVector(0,0));
        gameManager.getPlayers().get(0).addPawn(new HexVector(0,0));
        board.addPawn(pawn2, new HexVector(0,1));
        gameManager.getPlayers().get(1).addPawn(new HexVector(0,1));

        //at the beginning, both players should have 0 points
        assertEquals(0,gameManager.getPlayers().get(0).getPoints());
        assertEquals(0,gameManager.getPlayers().get(1).getPoints());



        Move move = new Move(new HexVector(0,0), new HexVector(1,0));
        gameManager.tryToMove(gameManager.getPlayers().get(0), move);

        move = new Move(new HexVector(0,1), new HexVector(1,1));
        gameManager.tryToMove(gameManager.getPlayers().get(1), move);

        move = new Move(new HexVector(1,0), new HexVector(2,0));
        gameManager.tryToMove(gameManager.getPlayers().get(0), move);

        assertEquals(2,gameManager.getPlayers().get(0).getPoints());
        verify(gameObserver,times(2)).onPlayerPointsUpdated(eq(gameManager.getPlayers().get(0)));
        assertEquals(1,gameManager.getPlayers().get(1).getPoints());
        verify(gameObserver,times(1)).onPlayerPointsUpdated(eq(gameManager.getPlayers().get(1)));
    }

    @Test
    void testGameEndsWhenNoValidMoves() {
        Board board = new Board(mock(BoardView.class), 8);
        board.addTile(new ScoreTile(1), new HexVector(0,1));
        board.addTile(new ScoreTile(1), new HexVector(0,0));
        board.addTile(new ScoreTile(1), new HexVector(1,1));

        MoveChecker moveChecker = new StandardMoveChecker(board);
        ArrayList<String> nicknames = new ArrayList<>();
        ArrayList<ControllerFactory> controllers = new ArrayList<>();
        for(int i=0; i<2; i++) {
            nicknames.add("player"+i);
            controllers.add(getFactoryOfMockedController());
        }
        GameManager gameManager = new GameManager(moveChecker, board, nicknames, controllers, 0);
        GameObserver gameObserver = mock(GameObserver.class);
        gameManager.setObserver(gameObserver);

        Pawn pawn1 = new Pawn(1);
        Pawn pawn2 = new Pawn(2);
        board.addPawn(pawn1, new HexVector(0,1));
        gameManager.getPlayers().get(0).addPawn(new HexVector(0,1));
        board.addPawn(pawn2, new HexVector(1,1));
        gameManager.getPlayers().get(1).addPawn(new HexVector(1,1));

        gameManager.tryToMove(gameManager.getPlayers().get(0), new Move(new HexVector(0,1), new HexVector(0,0)));

        verify(gameObserver, times(1)).onGameOver(any(), eq(false));
    }

    @Test
    void testGameSavesWithDefaultSettings() {
        Board board = new Board(mock(BoardView.class), DEFAULT_BOARD_SIZE);
        MoveChecker moveChecker = new StandardMoveChecker(board);
        ArrayList<String> nicknames = new ArrayList<>();
        ArrayList<ControllerFactory> controllers = new ArrayList<>();
        GameManager gameManager = new GameManager(moveChecker, board, nicknames, controllers, DEFAULT_PAWNS_PER_PLAYER);
        GameObserver gameObserver = mock(GameObserver.class);
        gameManager.setObserver(gameObserver);

        gameManager.endGame(true);

        verify(gameObserver, times(1)).onGameOver(any(), eq(true));
    }

    private ControllerFactory getFactoryOfMockedController() {
        return new ControllerFactory() {
            @Override
            public PlayerController newController() {
                return mock(PlayerController.class);
            }

            @Override
            public String toString() {
                return "";
            }

            @Override
            public void shutdown() {

            }
        };
    }
}
