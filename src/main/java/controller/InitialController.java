package controller;

import model.core.Country;
import model.simulation.ArrivalScheduler;
import model.simulation.Event;

import java.util.List;
import java.util.PriorityQueue;
import model.core.ResourceNode;
import model.core.Resource;
import model.core.ResourceCategory;

public class InitialController {
    private final List<Country> countries;
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>();
    private double currentTime = 0.0;
    private final ArrivalScheduler arrivalScheduler;

    public InitialController(List<Country> countries) {
        this.countries = countries;
        this.arrivalScheduler = new ArrivalScheduler(eventQueue, currentTime);
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void runSimulation(int timeSteps) {
        for (int i = 0; i < timeSteps; i++) {
            for (Country country : countries) {
                // Simulate resource allocation
                country.allocateResources();

                // Simulate trade between countries
                for (Country otherCountry : countries) {
                    if (!country.equals(otherCountry)) {
                        // Example trade logic (this should be expanded based on your requirements)
                        for (ResourceNode resourceNode : country.getResourceNodes()) {
                            Resource resource = new Resource(resourceNode.getName(), 0, ResourceCategory.FOOD, resourceNode.getBaseCapacity(), resourceNode.getProductionCost());
                            country.trade(otherCountry, resource);
                        }
                    }
                }
            }
        }
    }

    public void scheduleArrival(Country country, double lambda) {
        arrivalScheduler.scheduleArrival(country, lambda);
    }
}