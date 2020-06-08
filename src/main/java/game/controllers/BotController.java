package game.controllers;

import game.threads.ThreadRunner;
import util.Sleeper;

public interface BotController extends PlayerController {
    void setThreadRunner(ThreadRunner threadRunner);
    void setSleeper(Sleeper sleeper);
}
