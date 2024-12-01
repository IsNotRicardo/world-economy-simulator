package model.core;

import java.util.*;

public class Country {
    // Constants
    private static final int SUPPLY_ARCHIVE_TIME = 128;

    // Variables immediately initialized
    private final Map<Resource, Integer> resourceStorage = new HashMap<>();
    private final Map<Resource, int[]> supplyChanges = new HashMap<>();

    private final List<ResourceNode> resourceNodes = new ArrayList<>();
    private final List<Person> peopleObjects = new ArrayList<>();

    // Variables initialized in the constructor
    private final String name;
    private double money;
    private long population;

    public Country(String name, double initialMoney, int initialPopulation,
                   Map<Resource, Integer> starterResources, Map<Resource, ResourceNodeDTO> ownedResources) {
        this.name = name;
        this.money = initialMoney;
        this.population = initialPopulation;

        resourceStorage.putAll(starterResources);
        for (Resource resource : starterResources.keySet()) {
            supplyChanges.put(resource, new int[SUPPLY_ARCHIVE_TIME]);
        }

        for (Resource resource : ownedResources.keySet()) {
            resourceNodes.add(new ResourceNode(this, resource, ownedResources.get(resource)));
        }
    }

    // Start of Getters
    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public long getPopulation() {
        return population;
    }

    public List<ResourceNode> getResourceNodes() {
        return resourceNodes;
    }

    public List<Person> getPeopleObjects() {
        return peopleObjects;
    }

    public Map<Resource, Integer> getResourceStorage() {
        return resourceStorage;
    }

    public Map<Resource, int[]> getSupplyChanges() {
        return supplyChanges;
    }
    // End of Getters

    /*
    public void allocateResources() {
        // Allocate money to meet domestic resource demands
        for (Person person : peopleObjects) {
            // Logic to allocate resources based on person's demand
        }
    }

    public void trade(Country other, Resource resource) {
        // Simulate trade with another country
        // Logic to trade resources with another country
    }
    */
}