package game;

import board.Board;
import board.HexVector;
import board.drawable.pawn.Pawn;

import java.util.ArrayList;
import java.util.Collections;

public class PawnSetUpper {
    public void setUpPawns(Board board, ArrayList<Player> players, int pawnsPerPlayer) {
        int totalPawns = players.size() * pawnsPerPlayer;
        ArrayList<HexVector> tilePositions = new ArrayList<>(board.getTilePositions());
        Collections.shuffle(tilePositions);

        if(tilePositions.size() < totalPawns)
            throw new RuntimeException("too many pawns for this board size");

        for(int i=0;i<totalPawns;i++) {
            int whose = i/pawnsPerPlayer;
            Pawn pawn = new Pawn(whose+1);
            board.addPawn(pawn, tilePositions.get(i));
            players.get(whose).addPawn(tilePositions.get(i));
        }
    }
}
