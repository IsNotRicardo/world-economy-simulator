package model.core;

import java.util.Map;

public class Person {
    private final Country country;
    private final Map<String, Integer> demand;

    public Person(Country country, Map<String, Integer> demand) {
        this.country = country;
        this.demand = demand;
    }

    public Country getCountry() {
        return country;
    }

    public Map<String, Integer> getDemand() {
        return demand;
    }

    public void consumeResource(Resource resource) {
        // Simulate resource consumption logic
        // For example, reduce the quantity of the resource based on demand
    }

    public void generateDemand() {
        // Create demand for resources
        // For example, populate the demand map with resource types and quantities
    }
}