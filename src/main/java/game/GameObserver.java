package game;

public interface GameObserver {
    void onGameOver(boolean saveGame);
    void updatePlayerPoints(Player player);
}
