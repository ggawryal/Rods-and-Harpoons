package game.controllers.botcontrollerfactories;

import game.controllers.BotControllerFactory;
import game.controllers.strategies.BotStrategy;
import game.controllers.strategies.GreedyStrategy;
import game.threads.ThreadRunner;
import util.sleeper.Sleeper;

public class MediumBotControllerFactory extends BotControllerFactory {
    public MediumBotControllerFactory(Sleeper sleeper, ThreadRunner threadRunner) {
        super(sleeper, threadRunner);
    }

    @SuppressWarnings("unused")
    public MediumBotControllerFactory() {
        super();
    }

    @Override
    public String toString() {
        return "Medium Bot";
    }

    @Override
    public BotStrategy getStrategy() {
        return new GreedyStrategy();
    }
}
