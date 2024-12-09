package model.simulation;

public class Clock {
    private static Clock instance;
    private int time;
    private boolean paused;

    private Clock() {
        this.time = 0;
        this.paused = false;
    }

    // Singleton instance
    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    // Get the current time
    public int getTime() {
        return time;
    }

    // Set the current time
    public void setTime(int time) {
        this.time = time;
    }

    // Check if the clock is paused
    public boolean isPaused() {
        return paused;
    }

    // Pause the clock
    public void pause() {
        paused = true;
    }

    // Resume the clock
    public void resume() {
        paused = false;
    }
}