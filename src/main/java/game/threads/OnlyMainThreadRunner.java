package game.threads;

public class OnlyMainThreadRunner implements ThreadRunner {
    @Override
    public void runLaterInBackground(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void runLaterInMainThread(Runnable runnable) {
        runnable.run();
    }

    @Override
    public void shutdown() {

    }
}
