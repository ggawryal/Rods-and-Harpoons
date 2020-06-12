package util;

import board.HexVector;
import board.drawable.pawn.Pawn;
import board.drawable.tile.Tile;
import database.DBDocument;
import game.Player;
import org.bson.Document;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.HashMap;

public class GameInfo implements DBDocument {
    private final HashMap<HexVector, Tile> tiles;
    private final HashMap<HexVector, Pawn> pawns;
    private final ArrayList<Player> players;
    private final ArrayList<PlayerMove> playersMoves;

    public GameInfo(HashMap<HexVector, Tile> tiles, HashMap<HexVector, Pawn> pawns,
                    ArrayList<Player> players, ArrayList<PlayerMove> playersMoves) {
        this.tiles = new HashMap<>(tiles);
        this.pawns = new HashMap<>(pawns);
        this.players = new ArrayList<>(players);
        this.playersMoves = new ArrayList<>(playersMoves);
    }

    public HashMap<HexVector, Tile> getTiles() {
        return tiles;
    }

    public HashMap<HexVector, Pawn> getPawns() {
        return pawns;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<PlayerMove> getPlayersMoves() {
        return playersMoves;
    }

    private ArrayList<Document> getPlayersMovesAsDocuments() {
        ArrayList<Document> result = new ArrayList<>();
        for(var playerMove : playersMoves) {
            result.add(playerMove.toDocument());
        }
        return result;
    }

    private ArrayList<Document> getPlayersAsDocuments() {
        ArrayList<Document> result = new ArrayList<>();
        for(var player : players) {
            result.add(player.toDocument());
        }
        return result;
    }

    private ArrayList<Document> getTilesAsDocuments() {
        ArrayList<Document> result = new ArrayList<>();
        for(var entry : tiles.entrySet()) {
            result.add(new Document("position", entry.getKey().toDocument())
            .append("tile", entry.getValue().toDocument()));
        }
        return result;
    }

    private ArrayList<Document> getPawnsAsDocumentS() {
        ArrayList<Document> result = new ArrayList<>();
        for(var entry : pawns.entrySet()) {
            result.add(new Document("position", entry.getKey().toDocument())
                    .append("pawn", entry.getValue().toDocument()));
        }
        return result;
    }

    @Override
    public Document toDocument() {
        return new Document("tiles", getTilesAsDocuments())
                .append("pawns", getPawnsAsDocumentS())
                .append("players", getPlayersAsDocuments())
                .append("playersmoves", getPlayersMovesAsDocuments())
        ;
    }
}
