package game.controllers;

public class HumanControllerFactory implements ControllerFactory{
    @Override
    public PlayerController newController() {
        return new HumanController();
    }

    @Override
    public String toString() {
        return "Human";
    }

    @Override
    public void shutdown() {

    }
}
