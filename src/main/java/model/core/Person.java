package model.core;

import java.util.*;

public class Person {
    // Constants
    private final static double PREFERENCE_ADJUSTMENT_PROBABILITY = 0.2;
    private final static double PREFERENCE_ADJUSTMENT_RANGE = 0.1;

    // Variables immediately initialized
    private final Map<Resource, Double> preferences = new HashMap<>();
    private final Map<Resource, Integer> demand = new HashMap<>();

    // Variables initialized in the constructor
    private double happiness;
    private double budget;

    public Person(double initialHappiness, double initialBudget, Set<Resource> availableResources) {
        this.happiness = initialHappiness;
        this.budget = initialBudget;

        // Initialize the preferences map with random values
        Random random = new Random();
        for (Resource resource : availableResources) {
            preferences.put(resource, random.nextDouble());
        }
    }

    // Start of Getters
    public double getHappiness() {
        return happiness;
    }

    public double getBudget() {
        return budget;
    }

    public Map<Resource, Double> getPreferences() {
        return preferences;
    }
    // End of Getters

    void updatePerson() {
        adjustPreferences();
        generateDemand();
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

    /*
    public void consumeResource(Resource resource) {
        // Simulate resource consumption logic
        // For example, reduce the quantity of the resource based on demand
    }
    */
}