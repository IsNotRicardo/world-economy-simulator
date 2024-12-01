package model.simulation;

public class Clock {
    private static Clock instance;
    private long time;

    private Clock() {
        this.time = 0;  // Initialize time to zero or any desired default
    }

    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getTime() {
        return time;
    }

}
