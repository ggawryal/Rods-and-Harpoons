package arena;

import game.Player;

import java.util.ArrayList;
import java.util.Comparator;

public class WinStatistic implements Statistics {
    ArrayList<String> nicknames;
    int[] wins;
    int totalGames = 0;

    private int getPlayersNumber() {
        return nicknames.size();
    }

    public WinStatistic(ArrayList<String> nicknames) {
        this.nicknames = nicknames;
        wins = new int[nicknames.size()];
    }

    @Override
    public void collectGameResult(ArrayList<Player> players) {
        players.sort(Comparator.comparingInt(Player::getPoints).reversed());
        for(int i=0;i<getPlayersNumber();i++) {
            Player player = players.get(i);
            if(player.getPoints() == players.get(0).getPoints())
                wins[player.getId()]++;
        }
        totalGames++;
    }

    @Override
    public String getStatisticsFor(int i) {
        return String.format("Player %s (with id = %d) won %d/%d games [%.2f%%]\n",nicknames.get(i),i,wins[i],totalGames,wins[i]*100/(double)totalGames);
    }
}
