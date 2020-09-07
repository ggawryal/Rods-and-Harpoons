package game.controllers.botcontrollerfactories;

import game.controllers.BotControllerFactory;
import game.controllers.strategies.BotStrategy;
import game.controllers.strategies.MixedStrategy;
import game.threads.ThreadRunner;
import util.sleeper.Sleeper;


public class HardBotControllerFactory extends BotControllerFactory {
    public HardBotControllerFactory(Sleeper sleeper, ThreadRunner threadRunner) {
        super(sleeper, threadRunner);
    }

    @SuppressWarnings("unused")
    public HardBotControllerFactory() {
        super();
    }

    @Override
    public String toString() {
        return "Hard Bot";
    }

    @Override
    public BotStrategy getStrategy() {
        return new MixedStrategy();
    }
}
