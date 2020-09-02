package game.controllers;

import game.controllers.strategies.BotStrategy;
import game.threads.JavaFXThreadRunner;
import game.threads.ThreadRunner;
import util.sleeper.RealTimeSleeper;
import util.sleeper.Sleeper;



public abstract class BotFactory implements ControllerFactory{
    private final Sleeper sleeper;
    private final ThreadRunner threadRunner;

    public BotFactory(Sleeper sleeper, ThreadRunner threadRunner) {
        this.sleeper = sleeper;
        this.threadRunner = threadRunner;
    }

    @SuppressWarnings("unused")
    public BotFactory() {
        this(new RealTimeSleeper(), new JavaFXThreadRunner());
    }

    @Override
    public BotController newController() {
        return new BotController(getStrategy(), sleeper, threadRunner);
    }

    public abstract BotStrategy getStrategy();

}
