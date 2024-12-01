package controller;

import model.core.Country;
import model.simulation.ArrivalScheduler;
import model.simulation.Event;
import model.simulation.EventList;
import model.simulation.EventType;

import java.util.List;
import model.core.ResourceNode;
import model.core.Resource;
import model.core.ResourceCategory;

public class InitialController {
    private final List<Country> countries;
    private final EventList eventList = new EventList();
    private final ArrivalScheduler arrivalScheduler;

    public InitialController(List<Country> countries) {
        this.countries = countries;
        this.arrivalScheduler = new ArrivalScheduler(eventList.getEventQueue());
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void runSimulation(int timeSteps) {
        for (int i = 0; i < timeSteps; i++) {
            while (eventList.hasMoreEvents()) {
                Event event = eventList.getNextEvent();
                processEvent(event);
            }

            for (Country country : countries) {
                // Simulate resource allocation
                country.allocateResources();

                // Simulate trade between countries
                for (Country otherCountry : countries) {
                    if (!country.equals(otherCountry)) {
                        // Example trade logic (this should be expanded based on your requirements)
                        for (ResourceNode resourceNode : country.getResourceNodes()) {
                            Resource resource = new Resource(resourceNode.getName(), ResourceCategory.FOOD, 0.5, resourceNode.getBaseCapacity(), resourceNode.getProductionCost());
                            country.trade(otherCountry, resource);
                        }
                    }
                }
            }
        }
    }

    private void processEvent(Event event) {
        // Implement event processing logic here
        // For example, handle arrival events, trade events, etc.
    }

    /*private void processEvent(Event event) {
    switch (event.getType()) {
        case ARRIVAL:
            handleArrivalEvent(event);
            break;
        // Add cases for other event types as needed
        default:
            throw new IllegalArgumentException("Unknown event type: " + event.getType());
    }
}
*/

    public void scheduleArrival(Country country, double lambda) {
        arrivalScheduler.scheduleArrival(country, lambda);
    }
}