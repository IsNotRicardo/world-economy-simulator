package model.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Country {
    // Variables immediately initialized
    private final List<ResourceNode> resourceNodes = new ArrayList<>();
    private final List<Person> peopleObjects = new ArrayList<>();
    private final Map<Resource, Integer> resourceStorage = new HashMap<>();
    private final Map<Resource, int[]> supplyChanges = new HashMap<>();

    // Variables initialized in the constructor
    private final String name;
    private double money;
    private long population;

    public Country(String name, double initialMoney, int initialPopulation) {
        this.name = name;
        this.money = initialMoney;
        this.population = initialPopulation;
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