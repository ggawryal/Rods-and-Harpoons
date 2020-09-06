package game.controllers;

public interface ControllerFactory {
    PlayerController newController();
    String getName();
    void shutdown();
}
