package game.controllers.bot_factories;

import game.controllers.BotFactory;
import game.controllers.strategies.BotStrategy;
import game.controllers.strategies.GreedyStrategy;
import game.threads.ThreadRunner;
import util.sleeper.Sleeper;

public class EasyBotFactory extends BotFactory {
    public EasyBotFactory(Sleeper sleeper, ThreadRunner threadRunner) {
        super(sleeper, threadRunner);
    }

    @SuppressWarnings("unused")
    public EasyBotFactory() {
        super();
    }

    @Override
    public String getName() {
        return "Easy Bot";
    }

    @Override
    public BotStrategy getStrategy() {
        return new GreedyStrategy();
    }
}
