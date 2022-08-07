// Source: Ian @ https://stackoverflow.com/questions/32462251/how-to-check-if-1-second-has-passed-using-custom-deltatime-functions-java

public class Timer {
    // Last time in nanoseconds
    private static long second = 1000000000;
    private long lastTime;

    // Returns change in time as double
    private long getDeltaTime() {
        return (System.nanoTime() - lastTime);
    }

    // Updates lastTime
    private void updateTime() {
        this.lastTime = System.nanoTime();
    }

    // Public constructor for Timer object
    public Timer() {
        this.lastTime = System.nanoTime();
    }

    // Returns true if a second has passed and updates time,
    // otherwise returns false and does nothing.
    public boolean hasSecondPassed() {
        if (getDeltaTime() >= second) {
            updateTime();
            return true;
        } else
            return false;
    }
}