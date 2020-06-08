package util;

import java.util.concurrent.TimeUnit;

public class RealTimeSleeper implements Sleeper{
    @Override
    public void sleep(long millis) {
        try {
            TimeUnit.MILLISECONDS.sleep(millis);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
