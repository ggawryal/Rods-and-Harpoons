package database;

import util.GameInfo;

public interface Database {
    String saveGame(GameInfo gameInfo);
    void updateGame(String gameId, GameInfo gameInfo);
    void removeGame(String gameId);
}
