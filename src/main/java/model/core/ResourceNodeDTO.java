package model.core;

public record ResourceNodeDTO(int tier, int baseCapacity, double productionCost, Resource resource) {
    public ResourceNodeDTO {
        if (tier < 0) {
            throw new IllegalArgumentException("Tier cannot be negative.");
        }
        if (baseCapacity <= 0) {
            throw new IllegalArgumentException("Base capacity must be positive.");
        }
        if (productionCost < 0) {
            throw new IllegalArgumentException("Production cost cannot be negative.");
        }
    }
}
