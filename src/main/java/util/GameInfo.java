package util;

import board.HexVector;
import board.drawable.pawn.Pawn;
import board.drawable.tile.Tile;
import database.DBDocument;
import game.Player;
import game.controllers.ControllerFactory;
import org.bson.Document;


import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;

public class GameInfo implements DBDocument {
    private final HashMap<HexVector, Tile> tiles;
    private final HashMap<HexVector, Pawn> pawns;
    private final ArrayList<ControllerFactory> controllerFactories;
    private final ArrayList<Player> players;
    private final ArrayList<PlayerMove> playersMoves;
    private final boolean gameFinished;

    public int getTurn() {
        return turn;
    }

    private final int turn;

    public GameInfo(HashMap<HexVector, Tile> tiles, HashMap<HexVector, Pawn> pawns, List<ControllerFactory> controllerFactories,
                    List<Player> players, List<PlayerMove> playersMoves, int turn, boolean gameFinished) {
        this.tiles = new HashMap<>(tiles);
        this.pawns = new HashMap<>(pawns);
        this.controllerFactories = new ArrayList<>(controllerFactories);
        this.players = new ArrayList<>(players);
        this.playersMoves = new ArrayList<>(playersMoves);
        this.turn = turn;
        this.gameFinished = gameFinished;
    }

    public ArrayList<ControllerFactory> getControllerFactories() { return controllerFactories; }

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

    public boolean isGameFinished() { return gameFinished; }

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

    private ArrayList<Document> getControllersAsDocument() {
        ArrayList<Document> result = new ArrayList<>();
        for(ControllerFactory factory : controllerFactories) {
            result.add(new Document("type", factory.getClass().getCanonicalName()));
        }
        return result;
    }

    @Override
    public Document toDocument() {
        return new Document("tiles", getTilesAsDocuments())
                .append("pawns", getPawnsAsDocumentS())
                .append("controllers", getControllersAsDocument())
                .append("players", getPlayersAsDocuments())
                .append("playersMoves", getPlayersMovesAsDocuments())
                .append("turn", turn)
                .append("gameFinished", gameFinished);

    }

    public GameInfo(Document document) {
        tiles = new HashMap<>();
        for(Document doc : document.getList("tiles",Document.class)) {
            Document position = doc.get("position",Document.class);
            Document tile = doc.get("tile",Document.class);
            tiles.put(new HexVector(position), loadTile(tile));
        }

        pawns = new HashMap<>();
        for(Document doc : document.getList("pawns",Document.class)) {
            Document position = doc.get("position",Document.class);
            Document pawn = doc.get("pawn",Document.class);
            pawns.put(new HexVector(position), new Pawn(pawn));
        }

        controllerFactories = new ArrayList<>();
        for(Document doc : document.getList("controllers",Document.class))
            controllerFactories.add(loadControllerFactory(doc));

        players = new ArrayList<>();
        for(Document doc : document.getList("players",Document.class))
            players.add(new Player(doc));

        playersMoves = new ArrayList<>();
        for(Document doc : document.getList("playersMoves",Document.class))
            playersMoves.add(new PlayerMove(doc));

        turn = document.getInteger("turn");
        gameFinished = document.getBoolean("gameFinished");
    }

    private Tile loadTile(Document document) {
        try {
            String canonicalName  = document.getString("type");
            Class<?> c = Class.forName(canonicalName);
            Constructor<?> constructor = c.getConstructor(Document.class);
            return (Tile) constructor.newInstance(document);
        }catch(Exception ignored) {
            throw new RuntimeException("Unknown type of tile found");
        }
    }

    private ControllerFactory loadControllerFactory(Document document) {
        try {
            String canonicalName  = document.getString("type");
            Class<?> c = Class.forName(canonicalName);
            Constructor<?> constructor = c.getConstructor();
            return (ControllerFactory) constructor.newInstance();
        }catch(Exception ignored) {
            throw new RuntimeException("Unknown type of controller found");
        }
    }

}
