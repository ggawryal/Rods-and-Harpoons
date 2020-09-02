package game.controllers.bot_factories;

import game.controllers.BotFactory;
import game.controllers.strategies.BotStrategy;
import game.controllers.strategies.GreedyStrategy;
import game.threads.ThreadRunner;
import util.sleeper.Sleeper;

public class MediumBotFactory extends BotFactory {
    public MediumBotFactory(Sleeper sleeper, ThreadRunner threadRunner) {
        super(sleeper, threadRunner);
    }

    @SuppressWarnings("unused")
    public MediumBotFactory() {
        super();
    }

    @Override
    public String getName() {
        return "Medium Bot";
    }

    @Override
    public BotStrategy getStrategy() {
        return new GreedyStrategy();
    }
}
