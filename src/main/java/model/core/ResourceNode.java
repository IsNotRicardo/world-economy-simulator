package model.core;

public class ResourceNode {
    private final Country country;
    private final String name;
    private final int tier;
    private final int baseCapacity;
    private final double productionCost;
    private final Resource resource;

    public ResourceNode(Country country, Resource resource, ResourceNodeDTO resourceNodeDTO) {
        this.country = country;
        this.name = resourceNodeDTO.resource().name() + " Node";
        this.tier = resourceNodeDTO.tier();
        this.baseCapacity = resourceNodeDTO.baseCapacity();
        this.productionCost = resourceNodeDTO.productionCost();
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

    // This method needs to be modified to match the event types
    protected void produceResources(int quantity) {
        double availableMoney = country.getMoney();
        int producedQuantity = (int) Math.min(quantity, (int) availableMoney / productionCost);

        if (quantity > 0) {
            country.updateStorageAndFunds(resource, producedQuantity, producedQuantity * productionCost);
        }
    }
}