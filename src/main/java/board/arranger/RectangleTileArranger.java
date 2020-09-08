package board.arranger;

import board.Board;
import board.Direction;
import board.HexVector;

/**
 * Board has 'height' rows
 * Even rows have evenRowWidth tiles and odd have evenRowWidth-1
 */
public class RectangleTileArranger implements TileArranger {
    private int evenRowWidth,height;

    public RectangleTileArranger(int evenRowWidth, int height) {
        this.evenRowWidth = evenRowWidth;
        this.height = height;
    }

    @Override
    public void arrange(Board b, TileScoreChooser tileScoreChooser) {
        b.clear();
        tileScoreChooser.prepareTiles(getTotalTilesNeeded());
        HexVector rowVector = new HexVector(0,0);
        for(int row=0; row<height; row++) {
            HexVector colVector = rowVector.copy();

            int rowWidth = evenRowWidth;
            if(row % 2 == 1)
                rowWidth --;

            for(int col=0; col<rowWidth; col++) {
                b.addTile(tileScoreChooser.chooseTile(),colVector.copy());
                colVector = colVector.add(new HexVector(1,0));
            }

            if(row % 2 == 0)
                rowVector = rowVector.add(new HexVector(Direction.SE,1));
            else
                rowVector = rowVector.add(new HexVector(Direction.NE,-1));
        }
    }

    public int getTotalTilesNeeded() {
        int evenRowNumber = (height+1)/2, oddRowNumber = (height)/2;
        return evenRowNumber * evenRowWidth + oddRowNumber * (evenRowWidth-1);
    }
}
