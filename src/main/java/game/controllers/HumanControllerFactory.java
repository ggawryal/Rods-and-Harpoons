package game.controllers;

public class HumanControllerFactory implements ControllerFactory{
    @Override
    public PlayerController newController() {
        return new HumanController();
    }
    @Override
    public String getName() {return "Human";}
}
