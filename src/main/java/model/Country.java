package model;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private final String name;
    private final List<ResourceNode> resourceNodes = new ArrayList<>();
    private final List<Person> population = new ArrayList<>();
    private double money;

    public Country(String name, double money, int population) {
        this.name = name;
        this.money = money;
    }

    public String getName() {
        return name;
    }

    public List<ResourceNode> getResourceNodes() {
        return resourceNodes;
    }

    public List<Person> getPopulation() {
        return population;
    }

    public double getMoney() {
        return money;
    }

    public void setMoney(double money) {
        this.money = money;
    }

    public void allocateResources() {
        // Allocate money to meet domestic resource demands
        for (Person person : population) {
            // Logic to allocate resources based on person's demand
        }
    }

    public void trade(Country other, Resource resource) {
        // Simulate trade with another country
        // Logic to trade resources with another country
    }
}