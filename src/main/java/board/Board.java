package board;

import board.drawable.pawn.Pawn;
import board.drawable.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private HashMap<HexVector, Tile> tiles = new HashMap<>();
    private HashMap<HexVector, Pawn> pawns = new HashMap<>();
    private BoardView view;
    private Random r = new Random();

    public Board(BoardView view) {
        this.view = view;
    }

    private void addPawn(Pawn pawn, HexVector position) {
        pawns.put(position, pawn);
        view.drawPawn(pawn, position);
    }

    public void addPawns(int numOfPlayers, int numOfPawns) {
        ArrayList<HexVector> keysAsArray = new ArrayList<>(tiles.keySet());
        ArrayList<Integer> numbers = new ArrayList<>();
        for(int i = 1; i <= numOfPlayers; i++) {
            for(int j = 0; j < numOfPawns; j++) {
                int n = r.nextInt(keysAsArray.size());
                while (numbers.contains(n)) {
                    n = r.nextInt(keysAsArray.size());
                }
                numbers.add(n);
                HexVector position = keysAsArray.get(n);
                addPawn(new Pawn(i), position);
            }
        }
    }

    public int getNumOfPawns() {
        return pawns.size();
    }

    boolean hasPawnAt(HexVector position) {
        return pawns.containsKey(position);
    }

    public ArrayList<HexVector> getPawnsPositions(int id) {
        ArrayList<HexVector> result = new ArrayList<>();
        for(HexVector position : pawns.keySet()) {
            Pawn pawn = pawns.get(position);
            if(pawn.getId() == id)
                result.add(position);
        }
        return result;
    }

    public boolean hasTileAt(HexVector position) {
        return tiles.containsKey(position);
    }

    public Tile getTileAt(HexVector position) {
        return tiles.get(position);
    }

    public boolean addTile(Tile tile, HexVector position) {
        if(hasTileAt(position))
            return false;

        tiles.put(position,tile);
        view.drawTile(tile,position);
        return true;
    }

    public void removeTile(HexVector position) {
        tiles.remove(position);
        view.removeTile(position);
    }

    private void removePawn(HexVector position) {
        pawns.remove(position);
        view.removePawn(position);
    }

    public void movePawn(Move move) {
        int id = pawns.get(move.getFrom()).getId();
        switchPawnSelection(move.getFrom());
        removePawn(move.getFrom());
        addPawn(new Pawn(id), move.getTo());
    }

    public void switchPawnSelection(HexVector position) {
        view.switchPawnSelection(position);
        StandardMoveChecker moveChecker = new StandardMoveChecker(this);
        for(Move move : moveChecker.getPossibleMoves(position)) {
            view.switchTileGlow(move.getTo());
        }
    }

    public void clear() {
        for(HexVector position : tiles.keySet())
            view.removeTile(position);
        for(HexVector position : pawns.keySet())
            view.removePawn(position);
        tiles.clear();
        pawns.clear();
    }
}
