package model.simulation;

import model.core.*;

import java.util.*;

public class SimulatorTest {
    public static void main(String[] args) {
        // Create resources
        Resource food = new Resource("Food", 0.8, 100, 10.0);
        Resource water = new Resource("Water", 0.9, 100, 5.0);
        Resource energy = new Resource("Energy", 0.7, 100, 15.0);
        List<Resource> resources = Arrays.asList(food, water, energy);

        // Create countries
        Map<Resource, Integer> starterResources = new HashMap<>();
        starterResources.put(food, 100);
        starterResources.put(water, 100);
        starterResources.put(energy, 100);

        Map<Resource, ResourceNodeDTO> ownedResources = new HashMap<>();
        ownedResources.put(food, new ResourceNodeDTO(1, 100, 10.0, food));
        ownedResources.put(water, new ResourceNodeDTO(1, 100, 5.0, water));
        ownedResources.put(energy, new ResourceNodeDTO(1, 100, 15.0, energy));

        Country country1 = new Country("Country1", 100_000_000, 1_000_000, starterResources, ownedResources);
        Country country2 = new Country("Country2", 100_000_000, 1_000_000, starterResources, ownedResources);
        List<Country> countries = Arrays.asList(country1, country2);

        SimulationConfig.setSimulationTime(20);
        SimulationConfig.setSimulationDelay(1);

        // Create and run the simulator
//        Simulator simulator = new Simulator(resources, countries);
//        simulator.runSimulation();
    }
}