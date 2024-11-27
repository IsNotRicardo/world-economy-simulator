package model;

public class ResourceNode {
    private final String name;
    private final int tier;
    private final int baseCapacity;
    private final double productionCost;
    private final Resource resource;

    public ResourceNode(String name, int tier, int baseCapacity, double productionCost, Resource resource) {
        this.name = name;
        this.tier = tier;
        this.baseCapacity = baseCapacity;
        this.productionCost = productionCost;
        this.resource = resource;
    }

    public String getName() {
        return name;
    }

    public int getTier() {
        return tier;
    }

    public int getBaseCapacity() {
        return baseCapacity;
    }

    public double getProductionCost() {
        return productionCost;
    }

    public Resource getResource() {
        return resource;
    }

    public void produce(double budget) {
        if (budget >= productionCost) {
            // Logic to produce resources based on the allocated budget
            // For example, increase the output based on the budget
        }
    }
}