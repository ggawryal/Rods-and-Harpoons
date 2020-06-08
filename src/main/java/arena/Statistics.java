package arena;

import game.Player;

import java.util.ArrayList;

public interface Statistics {
    void collectGameResult(ArrayList<Player> players);
    String getBriefStatisticsInfo();
    String getFullStatisticsInfo();

}
