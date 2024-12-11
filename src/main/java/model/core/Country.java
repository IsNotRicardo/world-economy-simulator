package model.core;

import model.simulation.SimulationConfig;

import java.util.*;

public class Country {
    // Constants
    private static final int PERSON_INITIAL_HAPPINESS = 0;
    private static final int PERSON_BASE_BUDGET = 10;
    private static final double COUNTRY_PROFIT_MARGIN = 0.1;
    private static final double COUNTRY_INDIVIDUAL_TAX = 0.3;
    private static final double BASE_EXPORT_TAX = 0.2;

    // Variables immediately initialized
    private final Map<Resource, ResourceInfo> resourceStorage = new HashMap<>();
    private final List<ResourceNode> resourceNodes = new ArrayList<>();
    private final List<Person> peopleObjects = new ArrayList<>();

    // Variables initialized in the constructor
    private List<Country> allCountries;
    private final String name;
    private double money;
    private long population;

    public Country(String name, double initialMoney, long initialPopulation,
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
            double baseProductionCost;

            if (ownedResources.get(resource) != null) {
                baseProductionCost = ownedResources.get(resource).productionCost();
            } else {
                baseProductionCost = resource.productionCost();
            }


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

    public void addAllCountries(List<Country> allCountries) {
        this.allCountries = allCountries;
    }

    double getResourceValue(Resource resource) {
        return resourceStorage.get(resource).getValuePerUnit() * (1 + COUNTRY_PROFIT_MARGIN);
    }

    public double getSegmentBudget() {
        int totalTier = 0;

        for (ResourceNode resourceNode : resourceNodes) {
            totalTier += resourceNode.getTier();
        }

        double calculatedBudget = totalTier * PERSON_BASE_BUDGET * (1 - COUNTRY_INDIVIDUAL_TAX)
                * SimulationConfig.getPopulationSegmentSize();
        return Math.max(calculatedBudget, PERSON_BASE_BUDGET);
    }

    public double getAverageHappiness() {
        double totalHappiness = 0;

        for (Person person : peopleObjects) {
            totalHappiness += person.getHappiness();
        }

        return totalHappiness / peopleObjects.size();
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
        double budget = this.getSegmentBudget();

        for (Person person : peopleObjects) {
            person.servePerson(budget);
        }
    }

    public void requestResources() {
        for (ResourceInfo resourceInfo : resourceStorage.values()) {
            resourceInfo.archiveSupply();
        }

        Map<Resource, Integer> totalDemand = new HashMap<>();
        Map<Resource, Double> totalSupplyChange = new HashMap<>();
        Set<ResourceNode> upgradedNodes = new HashSet<>();

        // Get the average supply change for each resource
        for (Map.Entry<Resource, ResourceInfo> entry : resourceStorage.entrySet()) {
            Resource resource = entry.getKey();
            ResourceInfo resourceInfo = entry.getValue();
            int[] supplyArchive = resourceInfo.getSupplyArchive();
            double totalChange = 0;
            for (int i = 1; i < resourceInfo.getCurrentSize(); i++) {
                totalChange += supplyArchive[i] - supplyArchive[i - 1];
            }
            double averageChange = resourceInfo.getCurrentSize() > 1 ? totalChange / (resourceInfo.getCurrentSize() - 1) : 0;
            totalSupplyChange.put(resource, averageChange);
        }

        // Get the combined demand of all people for each resource
        for (Person person : peopleObjects) {
            for (Map.Entry<Resource, Integer> demandEntry : person.getDemand().entrySet()) {
                Resource resource = demandEntry.getKey();
                int demand = demandEntry.getValue();
                totalDemand.put(resource, totalDemand.getOrDefault(resource, 0) + demand);
            }
        }

        // Decide on production, upgrades, and trading
        for (Map.Entry<Resource, Integer> demandEntry : totalDemand.entrySet()) {
            Resource resource = demandEntry.getKey();
            int demand = demandEntry.getValue();
            double supplyChange = totalSupplyChange.getOrDefault(resource, 0.0);

            if (resourceStorage.get(resource).getQuantity() == 0) {
                // Resource is not in storage, try to produce it
                ResourceNode resourceNode = getNodeFromResource(resource);
                if (resourceNode != null) {
                    int maxCapacity = resourceNode.getMaxCapacity();
                    int quantityToProduce = (int) Math.ceil(supplyChange);
                    if (quantityToProduce <= maxCapacity) {
                        resourceNode.produceResources(quantityToProduce);
                    } else {
                        resourceNode.produceResources(maxCapacity);
                        if (this.money >= resourceNode.getUpgradeCost() && !upgradedNodes.contains(resourceNode)) {
                            resourceNode.upgradeNode();
                            upgradedNodes.add(resourceNode);
                        }
                    }
                } else {
                    // Resource node not available, trade for the resource
                    int currentQuantity = resourceStorage.get(resource).getQuantity();
                    int targetQuantity = currentQuantity + demand;
                    for (Country otherCountry : this.allCountries) {
                        if (otherCountry != this && currentQuantity < targetQuantity) {
                            otherCountry.requestTrade(this, resource, demand);
                            currentQuantity = resourceStorage.get(resource).getQuantity();
                        }
                    }
                }
            } else {
                // People don't have enough money, upgrade an important resource node
                upgradeImportantResourceNode(totalDemand, totalSupplyChange, upgradedNodes);
            }
        }
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

    private ResourceNode getNodeFromResource(Resource resource) {
        for (ResourceNode resourceNode : resourceNodes) {
            if (resourceNode.getResource().equals(resource)) {
                return resourceNode;
            }
        }
        return null;
    }

    private void upgradeImportantResourceNode(Map<Resource, Integer> totalDemand, Map<Resource, Double> totalSupplyChange, Set<ResourceNode> upgradedNodes) {
        Resource mostImportantResource = null;
        double maxDifference = Double.MIN_VALUE;

        for (Map.Entry<Resource, Integer> entry : totalDemand.entrySet()) {
            Resource resource = entry.getKey();
            int demand = entry.getValue();
            double supplyChange = totalSupplyChange.getOrDefault(resource, 0.0);
            double difference = demand - supplyChange;

            if (difference > maxDifference) {
                maxDifference = difference;
                mostImportantResource = resource;
            }
        }

        if (mostImportantResource != null) {
            ResourceNode resourceNode = getNodeFromResource(mostImportantResource);
            if (resourceNode != null && this.money >= resourceNode.getUpgradeCost() && !upgradedNodes.contains(resourceNode)) {
                resourceNode.upgradeNode();
                upgradedNodes.add(resourceNode);
            }
        }
    }

    void requestTrade(Country requestingCountry, Resource resource, int quantity) {
        ResourceInfo resourceInfo = resourceStorage.get(resource);
        double exportPrice = resourceInfo.getValuePerUnit() * (1 + BASE_EXPORT_TAX);
        int availableQuantity = resourceStorage.get(resource).getQuantity();
        int quantityToTrade = (int) Math.min(quantity, Math.min(availableQuantity, requestingCountry.getMoney() / exportPrice));
        double totalCost = quantityToTrade * exportPrice;

        if (quantityToTrade > 0) {
            removeResources(resource, quantityToTrade);
            addMoney(totalCost);
            requestingCountry.addResources(resource, quantityToTrade);
            requestingCountry.subtractMoney(totalCost);
        }
    }
}