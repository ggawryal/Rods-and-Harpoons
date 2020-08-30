package arena;

import game.Player;

import java.util.ArrayList;

/**
Interface for collecting various data from many games
 */
public interface Statistics {
    void collectGameResult(ArrayList<Player> players);
    String getStatisticsFor(int playerId);

}
