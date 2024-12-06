package model.core;

import model.simulation.SimulationConfig;

import java.util.*;

public class Country {
    // Constants
    private static final int PERSON_INITIAL_HAPPINESS = 0;
    private static final int PERSON_MINIMUM_BUDGET = 10;
    private static final double COUNTRY_PROFIT_MARGIN = 0.1;
    private static final double COUNTRY_INDIVIDUAL_TAX = 0.3;

    // Variables immediately initialized
    private final Map<Resource, ResourceInfo> resourceStorage = new HashMap<>();
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
        for (Map.Entry<Resource, Integer> entry : starterResources.entrySet()) {
            Resource resource = entry.getKey();
            int quantity = entry.getValue();
            double baseProductionCost = ownedResources.get(resource).productionCost();

            resourceStorage.put(resource, new ResourceInfo(quantity, quantity * baseProductionCost,
                    SimulationConfig.getSupplyArchiveTime()));
        }

        // Create resource nodes based on the owned resources
        for (Resource resource : ownedResources.keySet()) {
            resourceNodes.add(new ResourceNode(this, resource, ownedResources.get(resource)));
        }

        // Create people objects based on the initial population
        int numberOfPeople = (int) Math.ceil((double) initialPopulation / SimulationConfig.getPopulationSegmentSize());
        for (int i = 0; i < numberOfPeople; i++) {
            peopleObjects.add(new Person(this, PERSON_INITIAL_HAPPINESS, starterResources.keySet()));
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

    public Map<Resource, ResourceInfo> getResourceStorage() {
        return resourceStorage;
    }
    // End of Getters

    double getResourceValue(Resource resource) {
        return resourceStorage.get(resource).getValuePerUnit() * (1 + COUNTRY_PROFIT_MARGIN);
    }

    double getIndividualBudget() {
        double budget = 0.0;

        for (ResourceNode resourceNode : resourceNodes) {
            int storedResources = resourceNode.getStoredResources();

            if (storedResources > 0) {
                budget += storedResources * resourceNode.getProductionCost() * SimulationConfig.getPopulationSegmentSize();
            }
        }

        return budget * (1 - COUNTRY_INDIVIDUAL_TAX);
    }

    public void updatePeople() {
        updateNumberOfPeople();

        for (Person person : peopleObjects) {
            person.updatePerson();
        }
    }

    public void obtainResources() {
        for (ResourceNode resourceNode : resourceNodes) {
            resourceNode.collectResources();
        }
    }

    public void servePeople() {
        double budget = this.getIndividualBudget();

        for (Person person : peopleObjects) {
            person.servePerson(Math.min(budget, PERSON_MINIMUM_BUDGET));
        }
    }

    public void requestResources() {
        for (ResourceInfo resourceInfo : resourceStorage.values()) {
            resourceInfo.archiveSupply();
        }
        // Logic to buy or produce resources based on demand
        // Logic to request resources from other countries
        // Logic to upgrade resource nodes if needed
    }

    void addResources(Resource resource, int quantity) {
        this.resourceStorage.get(resource).addQuantity(quantity);
    }

    void removeResources(Resource resource, int quantity) {
        this.resourceStorage.get(resource).subtractQuantityAndValue(quantity);
    }

    double getResourceQuantity(Resource resource) {
        return this.resourceStorage.get(resource).getQuantity();
    }

    void addMoney(double amount) {
        this.money += amount;
    }

    void subtractMoney(double amount) {
        this.money -= amount;
    }

    void addPopulation(long population) {
        this.population += population;
    }

    void subtractPopulation(long population) {
        if (this.population < population) {
            this.population = 1;
            return;
        }
        this.population -= population;
    }

    private void updateNumberOfPeople() {
        int numberOfPeople = (int) Math.ceil((double) this.population / SimulationConfig.getPopulationSegmentSize());

        if (numberOfPeople > this.peopleObjects.size()) {
            for (int i = this.peopleObjects.size(); i < numberOfPeople; i++) {
                this.peopleObjects.add(new Person(this, PERSON_INITIAL_HAPPINESS, this.resourceStorage.keySet()));
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

    private class ResourceInfo {
        private int quantity;
        private double value;
        private final int[] supplyArchive;
        private int currentSize = 0;

        public ResourceInfo(int quantity, double value, int archiveTime) {
            this.quantity = quantity;
            this.value = value;
            this.supplyArchive = new int[archiveTime];
        }

        private int getQuantity() {
            return quantity;
        }

        private double getValue() {
            return value;
        }

        private int[] getSupplyArchive() {
            return supplyArchive;
        }

        private double getValuePerUnit() {
            return value / quantity;
        }

        private void addQuantity(int quantity) {
            this.quantity += quantity;
        }

        private void addValue(double value) {
            this.value += value;
        }

        private void subtractQuantityAndValue(int quantity) {
            int initialQuantity = this.quantity;
            double valueDifference = this.value / initialQuantity * quantity;

            this.quantity -= quantity;
            this.value -= valueDifference;
        }

        private void archiveSupply() {
            if (currentSize < supplyArchive.length) {
                currentSize++;
            }
            for (int i = currentSize - 1; i > 0; i--) {
                supplyArchive[i] = supplyArchive[i - 1];
            }
            supplyArchive[0] = this.getQuantity();
        }
    }
}