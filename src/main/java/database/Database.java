package database;

import game.Player;
import util.GameInfo;

import java.util.Collection;

public interface Database {
    String saveNewGame(GameInfo gameInfo);
    void updateGame(String gameId, GameInfo gameInfo);
    void removeGame(String gameId);
    void updatePlayersHighscores(Collection<Player> players, String gameId);
}
