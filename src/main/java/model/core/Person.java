package model.core;

import model.simulation.SimulationConfig;

import java.util.*;

public class Person {
    // Constants
    private final static double PREFERENCE_ADJUSTMENT_PROBABILITY = 0.2;
    private final static double PREFERENCE_ADJUSTMENT_RANGE = 0.1;
    private static final double MAX_HAPPINESS_CHANGE = 0.05;
    private static final double POPULATION_CHANGE_THRESHOLD = 0.6;
    private static final double POPULATION_CHANGE_PERCENTAGE = 0.01;

    // Variables immediately initialized
    private final Map<Resource, Double> preferences = new HashMap<>();
    private final Map<Resource, Integer> demand = new HashMap<>();

    // Variables initialized in the constructor
    private final Country country;
    private double happiness;

    public Person(Country country, double initialHappiness, Set<Resource> availableResources) {
        this.country = country;
        this.happiness = initialHappiness;

        // Initialize the preferences map with random values
        Random random = new Random();
        for (Resource resource : availableResources) {
            preferences.put(resource, random.nextDouble());
        }
        generateDemand();
    }

    // Start of Getters
    public double getHappiness() {
        return happiness;
    }

    public Map<Resource, Double> getPreferences() {
        return preferences;
    }

    public Map<Resource, Integer> getDemand() {
        return demand;
    }
    // End of Getters

    void updatePerson() {
        adjustPreferences();
        generateDemand();
    }

    void servePerson(double budget) {
        int totalDemand = demand.size();

//        System.out.println("Demand: " + demand);
        Iterator<Map.Entry<Resource, Integer>> demandIterator = demand.entrySet().iterator();
        while (demandIterator.hasNext()) {
            Map.Entry<Resource, Integer> entry = demandIterator.next();
            Resource resource = entry.getKey();
            int quantity = entry.getValue();
            System.out.println("Serving " + quantity + " units of " + resource);
            double totalCost = quantity * country.getResourceValue(resource);
            System.out.println("Total cost: " + totalCost);
            System.out.println("Budget: " + budget);

            if (country.getResourceQuantity(resource) >= quantity && budget >= totalCost) {
                country.removeResources(resource, quantity);
                country.addMoney(totalCost);
                budget -= totalCost;

                System.out.println("Served " + quantity + " units of " + resource + " for " + totalCost);
                demandIterator.remove();
            }
        }
//        System.out.println("Demand after serving: " + demand);

        double happinessChange = 0.0;
        if (totalDemand > 0) {
            double percentageFilled = (double) (totalDemand - demand.size()) / totalDemand;
            happinessChange = (percentageFilled * 2 - 1) * MAX_HAPPINESS_CHANGE;
        }

        happiness = Math.max(-1.0, Math.min(1.0, happiness + happinessChange));

        if (happiness > POPULATION_CHANGE_THRESHOLD) {
            country.addPopulation((long) (SimulationConfig.getPopulationSegmentSize() * POPULATION_CHANGE_PERCENTAGE));
        } else if (happiness < -POPULATION_CHANGE_THRESHOLD) {
            country.subtractPopulation((long) (SimulationConfig.getPopulationSegmentSize() * POPULATION_CHANGE_PERCENTAGE));
        }
    }

    private void adjustPreferences() {
        Random random = new Random();
        int totalPreferences = preferences.size();
        int preferencesToAdjust = (int) Math.ceil(PREFERENCE_ADJUSTMENT_PROBABILITY * totalPreferences);

        Set<Resource> resourceSet = preferences.keySet();
        List<Resource> resourceList = new ArrayList<>(resourceSet);
        Collections.shuffle(resourceList, random);

        for (int i = 0; i < preferencesToAdjust; i++) {
            Resource resource = resourceList.get(i);
            double currentPreference = preferences.get(resource);
            double adjustment = (random.nextDouble() - 0.5) * PREFERENCE_ADJUSTMENT_RANGE;
            double newPreference = Math.max(0.0, Math.min(1.0, currentPreference + adjustment));
            preferences.put(resource, newPreference);
        }
    }

    private void generateDemand() {
        int totalResources = preferences.size();
        int numberOfResources = Math.max(1, (int) Math.round((happiness + 1) / 2 * totalResources));

        demand.clear();
        Random random = new Random();
        Map<Resource, Double> weightedProbabilities = getWeighedProbabilities();

        List<Resource> resourceList = new ArrayList<>(weightedProbabilities.keySet());
        for (int i = 0; i < numberOfResources; i++) {
            double rand = random.nextDouble();
            double cumulativeProbability = 0.0;

            for (Resource resource : resourceList) {
                cumulativeProbability += weightedProbabilities.get(resource);
                if (rand <= cumulativeProbability) {
                    demand.put(resource, 1);
                    break;
                }
            }
        }
    }

    private Map<Resource, Double> getWeighedProbabilities() {
        Map<Resource, Double> weightedProbabilities = new HashMap<>();
        double totalWeight = 0.0;

        for (Map.Entry<Resource, Double> entry : preferences.entrySet()) {
            Resource resource = entry.getKey();
            double preference = entry.getValue();
            double weight = preference * resource.priority();
            weightedProbabilities.put(resource, weight);
            totalWeight += weight;
        }

        // Normalize the probabilities
        for (Map.Entry<Resource, Double> entry : weightedProbabilities.entrySet()) {
            weightedProbabilities.put(entry.getKey(), entry.getValue() / totalWeight);
        }

        return weightedProbabilities;
    }
}