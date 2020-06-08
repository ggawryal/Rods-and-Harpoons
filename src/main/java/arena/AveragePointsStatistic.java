package arena;

import game.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class AveragePointsStatistic implements Statistics {
    ArrayList<String> nicknames;
    double[] pointSum;
    int totalGames = 0;

    private int getPlayersNumber() {
        return nicknames.size();
    }

    public AveragePointsStatistic(ArrayList<String> nicknames) {
        this.nicknames = nicknames;
        pointSum = new double[nicknames.size()];
    }


    @Override
    public void collectGameResult(ArrayList<Player> players) {
        for(int i=0;i<getPlayersNumber();i++)
            pointSum[players.get(i).getId()] += players.get(i).getPoints();
        totalGames++;
    }

    @Override
    public String getStatisticsFor(int playerId) {
        return String.format("Average points for bot with id = %d was %.2f\n",playerId, pointSum[playerId]/totalGames);
    }
}
