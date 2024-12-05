package model.simulation;

public class Clock {
    private static Clock instance;
    private long time;
    private boolean running;

    private Clock() {
        this.time = 0; // Initialize time to zero or any desired default
        this.running = false; // Clock is not running initially
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
        new Thread(this::runClock).start(); // Run the clock in a separate thread
    }

    // Stop the clock
    public void stop() {
        running = false;
    }

    // Clock logic
    private void runClock() {
        int simulationDuration = SimulationConfig.getSimulationTime(); // Total days
        int delayPerDay = SimulationConfig.getSimulationDelay() * 1000; // Delay in milliseconds (converted from seconds)

        while (running && time < simulationDuration) {
            try {
                // Wait for the specified delay before advancing time
                Thread.sleep(delayPerDay);
                advanceTime();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted state
                System.out.println("Clock interrupted.");
            }
        }

        // Stop running when the simulation ends
        running = false;
        System.out.println("Simulation ended. Final time: Day " + time);
    }

    // Advance the simulation time by one day
    private void advanceTime() {
        time++;
        System.out.println("Simulation Time: Day " + time);
        // Notify other components if needed
    }

    // Get the current time
    public long getTime() {
        return time;
    }

    // Set the current time (useful for testing or resetting)
    public void setTime(long time) {
        this.time = time;
    }
}


/*package model.simulation;

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

} */
