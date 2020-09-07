package game.controllers.botcontrollerfactories;

import game.controllers.BotControllerFactory;
import game.controllers.strategies.BotStrategy;
import game.controllers.strategies.GreedyStrategy;
import game.controllers.strategies.RandomMoveStrategy;
import game.threads.ThreadRunner;
import util.sleeper.Sleeper;

public class EasyBotControllerFactory extends BotControllerFactory {
    public EasyBotControllerFactory(Sleeper sleeper, ThreadRunner threadRunner) {
        super(sleeper, threadRunner);
    }

    @SuppressWarnings("unused")
    public EasyBotControllerFactory() {
        super();
    }

    @Override
    public String toString() {
        return "Easy Bot";
    }

    @Override
    public BotStrategy getStrategy() {
        return new RandomMoveStrategy();
    }
}
