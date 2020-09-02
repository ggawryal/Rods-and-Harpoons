package game.controllers.bot_factories;

import game.controllers.BotFactory;
import game.controllers.strategies.BotStrategy;
import game.controllers.strategies.MixedStrategy;
import game.threads.ThreadRunner;
import util.sleeper.Sleeper;


public class HardBotFactory extends BotFactory{
    public HardBotFactory(Sleeper sleeper, ThreadRunner threadRunner) {
        super(sleeper, threadRunner);
    }

    @SuppressWarnings("unused")
    public HardBotFactory() {
        super();
    }

    @Override
    public String getName() {
        return "Hard Bot";
    }

    @Override
    public BotStrategy getStrategy() {
        return new MixedStrategy();
    }
}
