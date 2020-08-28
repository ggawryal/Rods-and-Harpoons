package game;

import util.GameInfo;

public interface GameObserver {
    void onGameOver(GameInfo gameInfo, boolean saveGame);
    void onPlayerPointsUpdated(Player player);
    void onGameInfoUpdated(GameInfo gameInfo);
}
