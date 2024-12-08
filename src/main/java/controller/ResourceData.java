package controller;

public record ResourceData(String name, int baseCapacity, double productionCost, int priority) {
	public ResourceData {
		if (baseCapacity <= 0) {
			throw new IllegalArgumentException("Base capacity must be positive.");
		}
		if (productionCost < 0) {
			throw new IllegalArgumentException("Production cost cannot be negative.");
		}
		if (priority < 0) {
			throw new IllegalArgumentException("Priority cannot be negative.");
		}
	}
}
