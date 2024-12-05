package model.core;

import model.simulation.SimulationConfig;

import java.util.*;

public class Country {
    // Constants
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
            supplyChanges.put(resource, new int[SimulationConfig.getSupplyArchiveTime()]);
        }

        // Create resource nodes based on the owned resources
        for (Resource resource : ownedResources.keySet()) {
            resourceNodes.add(new ResourceNode(this, resource, ownedResources.get(resource)));
        }

        // Create people objects based on the initial population
        int numberOfPeople = (int) Math.ceil((double) initialPopulation / SimulationConfig.getPopulationSegmentSize());
        for (int i = 0; i < numberOfPeople; i++) {
            peopleObjects.add(new Person(this, PERSON_INITIAL_HAPPINESS, PERSON_INITIAL_BUDGET, starterResources.keySet()));
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

    public double getResourcePrice() {
        // Logic to calculate the price of resources
        return 0;
    }

    public void obtainResources() {
        for (ResourceNode resourceNode : resourceNodes) {
            resourceNode.collectResources();
        }
    }

    public void servePeople() {
        for (Person person : peopleObjects) {
            person.servePerson();
        }
    }

    public void updatePeople() {
        updateNumberOfPeople();

        for (Person person : peopleObjects) {
            person.updatePerson();
        }
    }

    public void requestResources() {
        // Logic to buy or produce resources based on demand
        // Logic to request resources from other countries
        // Logic to upgrade resource nodes if needed
    }

    void addResources(Resource resource, int quantity) {
        this.resourceStorage.put(resource, this.resourceStorage.get(resource) + quantity);
    }

    void removeResources(Resource resource, int quantity) {
        this.resourceStorage.put(resource, this.resourceStorage.get(resource) - quantity);
    }

    void addMoney(double amount) {
        this.money += amount;
    }

    void subtractMoney(double amount) {
        this.money -= amount;
    }

    private void updateNumberOfPeople() {
        int numberOfPeople = (int) Math.ceil((double) this.population / SimulationConfig.getPopulationSegmentSize());

        if (numberOfPeople > this.peopleObjects.size()) {
            for (int i = this.peopleObjects.size(); i < numberOfPeople; i++) {
                this.peopleObjects.add(new Person(this, PERSON_INITIAL_HAPPINESS, PERSON_INITIAL_BUDGET, this.resourceStorage.keySet()));
            }
        } else if (numberOfPeople < this.peopleObjects.size()) {
            this.peopleObjects.subList(numberOfPeople, this.peopleObjects.size()).clear();
        }
    }

    /*

    public void trade(Country other, Resource resource) {
        // Simulate trade with another country
        // Logic to trade resources with another country
    }
    */
}