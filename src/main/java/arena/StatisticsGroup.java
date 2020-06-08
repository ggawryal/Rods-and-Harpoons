package arena;

import game.Player;

import java.util.ArrayList;

public class StatisticsGroup implements Statistics{
    ArrayList<Statistics> list = new ArrayList<>();
    private int playersNumber;

    public StatisticsGroup addStatistics(Statistics statistics) {
        list.add(statistics);
        return this;
    }

    @Override
    public void collectGameResult(ArrayList<Player> players) {
        for(Statistics stat : list)
            stat.collectGameResult(players);
    }

    @Override
    public String getStatisticsFor(int playerId) {
        StringBuilder sb = new StringBuilder();
        for(Statistics stat : list)
            sb.append(stat.getStatisticsFor(playerId));
        return sb.toString();
    }


    public String getStatsGroupedByPlayer() {
        StringBuilder sb = new StringBuilder();
        for(int i=0;i<getPlayersNumber();i++) {
            sb.append(getStatisticsFor(i)).append("\n");
        }
        return sb.toString();
    }

    public String getStatsGroupedByType() {
        StringBuilder sb = new StringBuilder();
        for(Statistics stat : list) {
            for (int i = 0; i < getPlayersNumber(); i++)
                sb.append(stat.getStatisticsFor(i));
            sb.append("\n");
        }
        return sb.toString();
    }

    public int getPlayersNumber() {
        return playersNumber;
    }

    public void setPlayersNumber(int playersNumber) {
        this.playersNumber = playersNumber;
    }
}
