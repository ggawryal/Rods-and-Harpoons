package board;

import board.drawable.pawn.Pawn;
import board.drawable.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Board {
    private HashMap<HexVector,Tile> tiles = new HashMap<>();
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

    public void addPawns(int count) {
        ArrayList<HexVector> keysAsArray = new ArrayList<>(tiles.keySet());
        ArrayList<Integer> ids = new ArrayList<>();
        for(int i = 1; i <= count; i++) {
            int num = r.nextInt(keysAsArray.size());
            while(ids.contains(num)) {
                num = r.nextInt(keysAsArray.size());
            }
            ids.add(num);
            HexVector position = keysAsArray.get(num);
            addPawn(new Pawn(i), position);
        }
    }

    public int getNumOfPawns() {
        return pawns.size();
    }

    boolean hasPawnAt(HexVector position) {
        return pawns.containsKey(position);
    }

    public HexVector getPawnPosition(int id) {
        for(HexVector position : pawns.keySet()) {
            Pawn pawn = pawns.get(position);
            if(pawn.getId() == id)
                return position;
        }
        return null;
    }



    boolean hasTileAt(HexVector position) {
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

    public void movePawn(HexVector from, HexVector to) {
        int id = pawns.get(from).getId();
        removePawn(from);
        addPawn(new Pawn(id), to);
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
