package util;

import board.HexVector;
import board.Move;
import board.drawable.pawn.Pawn;
import board.drawable.tile.EmptyTile;
import board.drawable.tile.ScoreTile;
import board.drawable.tile.Tile;
import game.Player;
import game.controllers.ControllerFactory;
import game.controllers.HumanControllerFactory;
import org.bson.Document;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class GameInfoTest {
    @Test
    void testGetters() {
        HashMap<HexVector, Tile> tiles = new HashMap<>();
        tiles.put(new HexVector(0,0), new EmptyTile());

        HashMap<HexVector, Pawn> pawns = new HashMap<>();
        pawns.put(new HexVector(0,0), new Pawn(1));

        List<ControllerFactory> controllerFactories = Collections.singletonList(new HumanControllerFactory());
        List<Player> players = Arrays.asList(new Player(0, "Pat"), new Player(1,"Mat"));

        List<PlayerMove> playersMoves = Collections.singletonList(
                new PlayerMove(0, 1, new Move(new HexVector(0, 0), new HexVector(0, 1)))
        );

        GameInfo gameInfo = new GameInfo(tiles, pawns, controllerFactories, players, playersMoves, 7, 3, 1, true);

        assertGameInfoEquals(gameInfo, tiles, pawns, controllerFactories, players, playersMoves, 7, 3, 1, true);
    }

    @Test
    void testFromAndToDocument() {
        HashMap<HexVector, Tile> tiles = new HashMap<>();
        tiles.put(new HexVector(0,0), new ScoreTile(2));

        HashMap<HexVector, Pawn> pawns = new HashMap<>();
        pawns.put(new HexVector(0,0), new Pawn(1));

        List<ControllerFactory> controllerFactories = Collections.singletonList(new HumanControllerFactory());
        List<Player> players = Arrays.asList(new Player(0, "Pat"), new Player(1,"Mat"));

        List<PlayerMove> playersMoves = Collections.singletonList(
                new PlayerMove(0, 1, new Move(new HexVector(0, 0), new HexVector(0, 1)))
        );

        GameInfo gameInfo = new GameInfo(tiles, pawns, controllerFactories, players, playersMoves, 15, 4, 1, false);
        Document document = gameInfo.toDocument();

        GameInfo gameInfoFromDocument = new GameInfo(document);
        assertGameInfoEquals(gameInfoFromDocument, tiles, pawns, controllerFactories, players, playersMoves, 15, 4, 1, false);
    }

    private void assertGameInfoEquals(
            GameInfo gameInfo,
            HashMap<HexVector, Tile> tiles,
            HashMap<HexVector, Pawn> pawns,
            List<ControllerFactory> controllerFactories,
            List<Player> players,
            List<PlayerMove> playersMoves,
            int boardSize,
            int pawnsPerPlayer,
            int turn,
            boolean hasGameEnded
    ) {
        assertEquals(tiles.size(), gameInfo.getTiles().size());
        for(Map.Entry<HexVector, Tile> entry : tiles.entrySet()) {
            assertTrue(gameInfo.getTiles().containsKey(entry.getKey()));
            assertEquals(entry.getValue().getScore(), gameInfo.getTiles().get(entry.getKey()).getScore());
        }

        assertEquals(pawns.size(), gameInfo.getPawns().size());
        for(Map.Entry<HexVector, Pawn> entry : pawns.entrySet()) {
            assertTrue(gameInfo.getPawns().containsKey(entry.getKey()));
            assertEquals(entry.getValue().getId(), gameInfo.getPawns().get(entry.getKey()).getId());
        }

        assertEquals(controllerFactories.size(), gameInfo.getControllerFactories().size());
        for(int i=0;i< controllerFactories.size(); i++)
            assertEquals(controllerFactories.get(i).getClass(), gameInfo.getControllerFactories().get(i).getClass());

        assertEquals(players.size(), gameInfo.getPlayers().size());
        for(int i=0;i<players.size();i++) {
            assertEquals(players.get(i), gameInfo.getPlayers().get(i));
            assertEquals(players.get(i).getPoints(), gameInfo.getPlayers().get(i).getPoints());
        }

        assertEquals(playersMoves, gameInfo.getPlayersMoves());
        assertEquals(boardSize, gameInfo.getBoardSize());
        assertEquals(pawnsPerPlayer, gameInfo.getPawnsPerPlayer());
        assertEquals(turn, gameInfo.getTurn());
        assertEquals(hasGameEnded, gameInfo.isGameFinished());
    }
}