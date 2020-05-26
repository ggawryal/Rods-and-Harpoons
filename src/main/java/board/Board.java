package board;

import board.tile.Tile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class Board {
    private HashMap<HexVector,Tile> tiles = new HashMap<>();
    private BoardView view;
    private Random r = new Random();

    public Board(BoardView view) {
        this.view = view;
    }

    public HexVector getRandomPosition() {
        List<HexVector> keysAsArray = new ArrayList<>(tiles.keySet());
        return keysAsArray.get(r.nextInt(keysAsArray.size()));
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

    public void moveTile(HexVector from, HexVector to){
        Tile tile = tiles.get(from);
        tiles.remove(from);

        view.removeTile(from);
        view.removeTile(to);

        tiles.put(to, tile);
        view.drawTile(tile, to);
    }

    public void removeTile(HexVector position) {
        tiles.remove(position);
        view.removeTile(position);
    }

    public void clear() {
        for(HexVector position : tiles.keySet())
            view.removeTile(position);
        tiles.clear();
    }
}
