package game.controllers;

public interface ControllerFactory {
    PlayerController newController();
    void shutdown();
}
