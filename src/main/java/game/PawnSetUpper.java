package game;

import board.Board;
import board.HexVector;
import board.drawable.pawn.Pawn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;


public class PawnSetUpper {
    /**
     * Selects random tiles for pawns, but all selected tiles have 1 score
     */
    public void setUpPawns(Board board, ArrayList<Player> players, int pawnsPerPlayer) {
        int totalPawns = players.size() * pawnsPerPlayer;
        ArrayList<HexVector> tilePositions = new ArrayList<>(board.getTilePositions());
        Collections.shuffle(tilePositions);

        Iterator<HexVector> it =  tilePositions.iterator();
        int pawnsAdded = 0;
        while(pawnsAdded<totalPawns && it.hasNext()) {
            HexVector position = it.next();
            if(board.getTileAt(position).getScore() > 1)
                continue;
            int whose = pawnsAdded/pawnsPerPlayer;
            Pawn pawn = new Pawn(whose+1);
            board.addPawn(pawn, position);
            players.get(whose).addPawn(position);
            pawnsAdded++;
        }
        if(pawnsAdded < totalPawns)
            throw new RuntimeException("Not enough one point tiles for given number of players");
    }
}
