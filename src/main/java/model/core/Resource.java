package model.core;

public class Resource {
    private final String name;
    private final ResourceCategory category;
    private final double priority;
    private final int baseCapacity;
    private final double productionCost;

    public Resource(String name, ResourceCategory category, double priority, int baseCapacity, double productionCost) {
        this.name = name;
        this.category = category;
        this.priority = priority;
        this.baseCapacity = baseCapacity;
        this.productionCost = productionCost;
    }

    public String getName() {
        return name;
    }

    public double getPriority() {
        return priority;
    }

    public ResourceCategory getCategory() {
        return category;
    }

    public int getBaseCapacity() {
        return baseCapacity;
    }

    public double getProductionCost() {
        return productionCost;
    }
}