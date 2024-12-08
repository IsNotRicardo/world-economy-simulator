package model.simulation;

public class Clock {
    private static Clock instance;
    private int time;
    private boolean running;
    private boolean paused;

    private Clock() {
        this.time = 0; // Initialize time to zero or any desired default
        this.running = false; // Clock is not running initially
        this.paused = false; // Clock is not paused initially
    }

    // Singleton instance
    public static Clock getInstance() {
        if (instance == null) {
            instance = new Clock();
        }
        return instance;
    }

    // Start the clock
    public void start() {
        running = true;
        paused = false;
    }

    // Stop the clock
    public void stop() {
        running = false;
        paused = false;
    }

    // Pause the clock
    public void pause() {
        paused = true;
    }

    // Resume the clock
    public void resume() {
        paused = false;
    }

    // Advance the simulation time by one day
    public void advanceTime() {
        if (running && !paused) {
            time++;
            System.out.println("Simulation Time: Day " + time);
            // Notify other components if needed
        }
    }

    // Get the current time
    public int getTime() {
        return time;
    }

    // Set the current time (useful for testing or resetting)
    public void setTime(int time) {
        this.time = time;
    }

    // Check if the clock is running
    public boolean isRunning() {
        return running;
    }

    // Check if the clock is paused
    public boolean isPaused() {
        return paused;
    }
}