package model.core;

public class ResourceNode {
    private final Country country;
    private final String name;
    private final int tier;
    private final int baseCapacity;
    private final double productionCost;
    private final Resource resource;

    public ResourceNode(Country country, Resource resource) {
        this.country = country;
        this.name = resource.name();
        this.tier = 0;
        this.baseCapacity = resource.baseCapacity();
        this.productionCost = resource.productionCost();
        this.resource = resource;
    }

    public ResourceNode(Country country, String name, int tier, int baseCapacity, double productionCost,
                        Resource resource) {
        this.country = country;
        this.name = name;
        this.tier = tier;
        this.baseCapacity = baseCapacity;
        this.productionCost = productionCost;
        this.resource = resource;
    }

    // Start of Getters
    public Country getCountry() {
        return country;
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
    // End of Getters

    private void produceResources(int quantity) {
        double availableMoney = country.getMoney();

        if (availableMoney < productionCost * quantity) {
            //
        }
    }
}