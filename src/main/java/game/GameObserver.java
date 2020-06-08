package game;

public interface GameObserver {
    void onGameOver();
    void updatePlayerPoints(Player player);
}
