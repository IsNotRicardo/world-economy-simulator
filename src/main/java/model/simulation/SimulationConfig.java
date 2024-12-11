package model.simulation;

public class SimulationConfig {
    private static int simulationTime = 100;
    private static int simulationDelay = 100;
    private static int supplyArchiveTime = 128;
    private static int populationSegmentSize = 100_000;

    // Start of Getters
    public static int getSimulationTime() {
        return simulationTime;
    }

    public static int getSimulationDelay() {
        return simulationDelay;
    }

    public static int getSupplyArchiveTime() {
        return supplyArchiveTime;
    }

    public static int getPopulationSegmentSize() {
        return populationSegmentSize;
    }
    // End of Getters

    // Start of Setters
    public static void setSimulationTime(int newSimulationTime) {
        if (newSimulationTime <= 0) {
            throw new IllegalArgumentException("Simulation time must be positive");
        }
        simulationTime = newSimulationTime;
    }

    public static void setSimulationDelay(int newSimulationDelay) {
        if (newSimulationDelay <= 0) {
            throw new IllegalArgumentException("Simulation delay must be positive");
        }
        simulationDelay = newSimulationDelay;
    }

    public static void setSupplyArchiveTime(int newSupplyArchiveTime) {
        if (newSupplyArchiveTime <= 0) {
            throw new IllegalArgumentException("Supply archive time must be positive");
        }
        supplyArchiveTime = newSupplyArchiveTime;
    }

    public static void setPopulationSegmentSize(int newPopulationSegmentSize) {
        if (newPopulationSegmentSize <= 0) {
            throw new IllegalArgumentException("Population segment size must be positive");
        }
        populationSegmentSize = newPopulationSegmentSize;
    }
    // End of Setters
}
