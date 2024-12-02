package model.core;

import java.util.*;

public class Country {
    // Constants
    private static final int SUPPLY_ARCHIVE_TIME = 128;
    private static final int POPULATION_SEGMENT_SIZE = 100_000;

    private static final int PERSON_INITIAL_HAPPINESS = 0;
    private static final int PERSON_INITIAL_BUDGET = 100;

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
        if (initialMoney < 0) {
            throw new IllegalArgumentException("Initial money cannot be negative.");
        }
        if (initialPopulation <= 0) {
            throw new IllegalArgumentException("Initial population must be positive.");
        }

        this.name = name;
        this.money = initialMoney;
        this.population = initialPopulation;

        // Initialize the resource storage and supply changes
        resourceStorage.putAll(starterResources);
        for (Resource resource : starterResources.keySet()) {
            supplyChanges.put(resource, new int[SUPPLY_ARCHIVE_TIME]);
        }

        // Create resource nodes based on the owned resources
        for (Resource resource : ownedResources.keySet()) {
            resourceNodes.add(new ResourceNode(this, resource, ownedResources.get(resource)));
        }

        // Create people objects based on the initial population
        int numberOfPeople = (int) Math.ceil((double) initialPopulation / POPULATION_SEGMENT_SIZE);
        for (int i = 0; i < numberOfPeople; i++) {
            peopleObjects.add(new Person(PERSON_INITIAL_HAPPINESS, PERSON_INITIAL_BUDGET));
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

    public void updateStorageAndFunds(Resource resource, int quantity, double cost) {
        this.money -= cost;
        this.resourceStorage.put(resource, this.resourceStorage.get(resource) + quantity);
    }

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