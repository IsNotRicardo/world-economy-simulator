package model;

import java.util.Map;
import java.util.UUID;

public class Person {
    private final String id;
    private final Country country;
    private final Map<String, Integer> demand;

    public Person(Country country, Map<String, Integer> demand) {
        this.id = UUID.randomUUID().toString();
        this.country = country;
        this.demand = demand;
    }

    public String getId() {
        return id;
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