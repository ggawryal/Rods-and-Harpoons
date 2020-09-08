package util;

import board.HexVector;
import board.Move;
import database.DBDocument;
import game.Player;
import org.bson.Document;

public class PlayerMove implements DBDocument {
    private final int playerId;
    private final int points;
    private final Move move;

    public PlayerMove(int playerId, int points, Move move) {
        this.playerId = playerId;
        this.points = points;
        this.move = move;
    }

    public Move getMove() {
        return move;
    }

    public int getPlayerId() {
        return playerId;
    }

    public int getPoints() { return points; }

    @Override
    public Document toDocument() {
        return new Document("playerid", playerId)
                .append("points", points)
                .append("from", move.getFrom().toDocument())
                .append("to", move.getTo().toDocument());
    }

    public PlayerMove(Document document) {
        this(document.getInteger("playerid"), document.getInteger("points"),
                new Move(new HexVector(document.get("from",Document.class)), new HexVector(document.get("to",Document.class))));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PlayerMove playerMove = (PlayerMove) o;
        return playerId == playerMove.getPlayerId()
                && points == playerMove.getPoints()
                && move.getFrom().equals(playerMove.getMove().getFrom())
                && move.getTo().equals(playerMove.getMove().getTo());
    }
}
