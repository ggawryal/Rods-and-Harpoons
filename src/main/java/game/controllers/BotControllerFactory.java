package game.controllers;

import game.controllers.strategies.BotStrategy;
import game.threads.JavaFXThreadRunner;
import game.threads.ThreadRunner;
import util.sleeper.RealTimeSleeper;
import util.sleeper.Sleeper;

public abstract class BotControllerFactory implements ControllerFactory {
    private final Sleeper sleeper;
    private final ThreadRunner threadRunner;

    public BotControllerFactory(Sleeper sleeper, ThreadRunner threadRunner) {
        this.sleeper = sleeper;
        this.threadRunner = threadRunner;
    }

    @SuppressWarnings("unused")
    public BotControllerFactory() {
        this(new RealTimeSleeper(), new JavaFXThreadRunner());
    }

    @Override
    public BotController newController() {
        return new BotController(getStrategy(), sleeper, threadRunner);
    }

    public abstract BotStrategy getStrategy();

    @Override
    public void shutdown() {
        threadRunner.shutdown();
    }
}
