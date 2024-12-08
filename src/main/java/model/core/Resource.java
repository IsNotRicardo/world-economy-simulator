package model.core;

public record Resource (String name, ResourceCategory category, double priority, int baseCapacity,
                        double productionCost) {

    public Resource {
        if (priority < 0 || priority > 1) {
            throw new IllegalArgumentException("Priority must be between 0 and 1.");
        }
        if (baseCapacity <= 0) {
            throw new IllegalArgumentException("Base capacity must be positive.");
        }
        if (productionCost < 0) {
            throw new IllegalArgumentException("Production cost cannot be negative.");
        }
    }
}