package board;

import board.tile.Tile;

import java.util.HashMap;

public class Board {
    HashMap<HexVector,Tile> tiles = new HashMap<>();
    BoardView view;

    public Board(BoardView view) {
        this.view = view;
    }

    boolean hasTileAt(HexVector position) {
        return tiles.containsKey(position);
    }

    Tile getTileAt(HexVector position) {
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
}
