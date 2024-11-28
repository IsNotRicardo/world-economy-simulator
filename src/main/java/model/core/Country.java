package model.core;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Country {
    private final String name;
    private double money;
    private final long population;
    private final List<ResourceNode> resourceNodes = new ArrayList<>();
    private final List<Person> people = new ArrayList<>();
    private final Map<Resource, Integer> resourceStorage = new HashMap<>();
    private final Map<Resource, List<Integer>> supplyChanges = new HashMap<>();

    public Country(String name, double money, int population) {
        this.name = name;
        this.money = money;
        this.population = population;
    }

    public String getName() {
        return name;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public long getPopulation() {
        return population;
    }

    public List<ResourceNode> getResourceNodes() {
        return resourceNodes;
    }

    public List<Person> getPeople() {
        return people;
    }

    public Map<Resource, Integer> getResourceStorage() {
        return resourceStorage;
    }

    public Map<Resource, List<Integer>> getSupplyChanges() {
        return supplyChanges;
    }

    public void allocateResources() {
        // Allocate money to meet domestic resource demands
        for (Person person : people) {
            // Logic to allocate resources based on person's demand
        }
    }

    public void trade(Country other, Resource resource) {
        // Simulate trade with another country
        // Logic to trade resources with another country
    }
}