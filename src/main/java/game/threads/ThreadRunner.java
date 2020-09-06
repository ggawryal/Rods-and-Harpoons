package game.threads;

public interface ThreadRunner {
    void runLaterInBackground(Runnable runnable);
    void runLaterInMainThread(Runnable runnable);
    void shutdown();
}
