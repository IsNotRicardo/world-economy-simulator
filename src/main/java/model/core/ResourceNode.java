package model.core;

import model.simulation.SimulationConfig;

public class ResourceNode {
    // Constants
    private static final double MAX_REDUCTION_PERCENTAGE = 0.5;

    // Variables immediately initialized
    private int storedResources = 0;
    private int daysSinceLastProduction = 0;

    // Variables initialized in the constructor
    private final Country country;
    private final String name;
    private final int baseCapacity;
    private final double baseProductionCost;
    private final Resource resource;

    private int tier;

    public ResourceNode(Country country, Resource resource, ResourceNodeDTO resourceNodeDTO) {
        this.country = country;
        this.name = resourceNodeDTO.resource().name() + " Node";
        this.baseCapacity = resourceNodeDTO.baseCapacity();
        this.resource = resource;
        this.baseProductionCost = resourceNodeDTO.productionCost();
        this.tier = resourceNodeDTO.tier();
    }

    // Start of Getters
    public Country getCountry() {
        return country;
    }

    public String getName() {
        return name;
    }

    public int getBaseCapacity() {
        return baseCapacity;
    }

    public Resource getResource() {
        return resource;
    }

    public double getBaseProductionCost() {
        return baseProductionCost;
    }

    public int getTier() {
        return tier;
    }
    // End of Getters

    int getMaxCapacity() {
        return (int) Math.round(baseCapacity * (1 + tier * 0.1));
    }

    double getUpgradeCost() {
        return (tier + 1) * baseProductionCost * SimulationConfig.getPopulationSegmentSize();
    }

    double adjustProductionCost() {
        double reductionFactor = Math.min(daysSinceLastProduction * 0.01, MAX_REDUCTION_PERCENTAGE);
        return baseProductionCost * (1 - reductionFactor);
    }

    void collectResources() {
        if (storedResources != 0) {
            country.updateResourceStorage(resource, storedResources);
        } else {
            daysSinceLastProduction++;
        }
        adjustProductionCost();
    }

    void produceResources(int quantity) {
        double availableMoney = country.getMoney();
        double actualProductionCost = adjustProductionCost();
        int producedQuantity = (int) Math.min(quantity, Math.min(getMaxCapacity(), availableMoney / actualProductionCost));

        if (quantity > 0) {
            country.subtractMoney(producedQuantity * actualProductionCost);
            storedResources += producedQuantity;
            daysSinceLastProduction = 0;
        }
    }

    void upgradeNode() {
        double upgradeCost = getUpgradeCost();
        if (country.getMoney() >= upgradeCost) {
            country.subtractMoney(upgradeCost);
            tier++;
        }
    }
}