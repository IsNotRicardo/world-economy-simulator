package model;

public class Resource {
    private final String name;
    private final double priority;
    private final ResourceCategory category;
    private final int baseCapacity;
    private final double productionCost;

    public Resource(String name, double priority, ResourceCategory category, int baseCapacity, double productionCost) {
        this.name = name;
        this.priority = priority;
        this.category = category;
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