package model.simulation;

import controller.SimulationController;
import model.core.*;

import java.util.List;
import java.util.Map;

public class Simulator {
    private final Clock clock = Clock.getInstance();
    private final EventList eventList = new EventList();

    private final SimulationController simulationController;
    private final List<Resource> resources;
    private final List<Country> countries;

    public Simulator(SimulationController simulationController, List<Resource> resources, List<Country> countries) {
        this.simulationController = simulationController;
        this.resources = resources;
        this.countries = countries;

        for (Country country : countries) {
            country.addAllCountries(countries);
        }
    }

    public void runSimulation() {
        initializeSimulation();

        while (clock.getTime() < SimulationConfig.getSimulationTime()) {
            if (!clock.isPaused()) {
                // A-phase
                // Advance the clock to the next event time
                Event nextEvent = eventList.peekNextEvent();
                if (nextEvent != null) {
                    clock.setTime(nextEvent.getTime());
                }

                // B-phase
                // Process all events that are scheduled to occur at the current time
                while (nextEvent != null && nextEvent.getTime() == clock.getTime()) {
                    processEvent(eventList.getNextEvent());
                    nextEvent = eventList.peekNextEvent();
                }

                // C-phase
                // Create new events based on specific conditions
                // Currently, there are no available conditions to check

                saveMetrics();
                updateController();
                System.out.println("\nDay " + clock.getTime() + " completed.");
            }
            try {
                Thread.sleep(SimulationConfig.getSimulationDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Simulation interrupted.");
            }
        }

        finalizeSimulation();
    }

    private void initializeSimulation() {
        // Since all current events happen daily, adding +1 to the current time is sufficient
        int nextEventTime = clock.getTime() + 1;

        eventList.addEvent(new Event(EventType.UPDATE_PEOPLE, nextEventTime));
        eventList.addEvent(new Event(EventType.OBTAIN_RESOURCES, nextEventTime));
        eventList.addEvent(new Event(EventType.SERVE_PEOPLE, nextEventTime));
        eventList.addEvent(new Event(EventType.REQUEST_RESOURCES, nextEventTime));

        System.out.println("Simulation initialized.");
    }

    private void processEvent(Event event) {
        int nextEventTime = clock.getTime() + 1;

        switch (event.getType()) {
            case UPDATE_PEOPLE:
                for (Country country : countries) {
                    country.updatePeople();
                }
                eventList.addEvent(new Event(EventType.UPDATE_PEOPLE, nextEventTime));
                break;
            case OBTAIN_RESOURCES:
                for (Country country : countries) {
                    country.obtainResources();
                }
                eventList.addEvent(new Event(EventType.OBTAIN_RESOURCES, nextEventTime));
                break;
            case SERVE_PEOPLE:
                for (Country country : countries) {
                    country.servePeople();
                }
                eventList.addEvent(new Event(EventType.SERVE_PEOPLE, nextEventTime));
                break;
            case REQUEST_RESOURCES:
                for (Country country : countries) {
                    country.requestResources();
                }
                eventList.addEvent(new Event(EventType.REQUEST_RESOURCES, nextEventTime));
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + event.getType());
        }
    }

    private void saveMetrics() {
        for (Country country : countries) {
            // Save the country metrics
            // Call the DAO here
            System.out.println("\nSaving metrics for " + country.getName());
            System.out.println("Population: " + country.getPopulation());
            System.out.println("Money: " + country.getMoney());

//            // Save the resource metrics per country
//            for (Map.Entry<Resource, ResourceInfo> entry : country.getResourceStorage().entrySet()) {
//                // Call the DAO here
//                Resource resource = entry.getKey();
//                ResourceInfo resourceInfo = entry.getValue();
//                System.out.println("\nResource: " + resource.name());
//                System.out.println("--- Quantity: " + resourceInfo.getQuantity());
//                System.out.println("--- Value: " + resourceInfo.getValue());
//            }
//
//            // Save the resource node metrics per country
//            for (ResourceNode resourceNode : country.getResourceNodes()) {
//                // Call the DAO here
//                System.out.println("\nResource Node: " + resourceNode.getResource().name());
//                System.out.println("--- Stored Resources: " + resourceNode.getStoredResources());
//                System.out.println("--- Base Capacity: " + resourceNode.getBaseCapacity());
//                System.out.println("--- Tier: " + resourceNode.getTier());
//            }

//            for (Person person : country.getPeopleObjects()) {
//                System.out.println("Happiness: " + person.getHappiness());
//                System.out.println("Preferences: " + person.getPreferences());
//                System.out.println("Demand: " + person.getDemand());
//            }
        }
    }

    private void updateController() {
        simulationController.updateData();
    }

    private void finalizeSimulation() {
        // Generate a report or summary of the simulation results
        generateReport();

        System.out.println("\nSimulation finalized.");
    }

    private void generateReport() {
        // Implement logic to generate a report or summary of the simulation results
        System.out.println("\nGenerating simulation report...");
        // Example: Print summary statistics
        for (Country country : countries) {
            System.out.println("Country: " + country.getName());
            System.out.println("Population: " + country.getPopulation());
            System.out.println("Money: " + country.getMoney());

            for (Map.Entry<Resource, ResourceInfo> entry : country.getResourceStorage().entrySet()) {
                Resource resource = entry.getKey();
                ResourceInfo resourceInfo = entry.getValue();
                System.out.println("Resource: " + resource.name());
                System.out.println("--- Quantity: " + resourceInfo.getQuantity());
                System.out.println("--- Value: " + resourceInfo.getValue());
            }

            for (ResourceNode resourceNode : country.getResourceNodes()) {
                System.out.println("Resource Node: " + resourceNode.getResource().name());
                System.out.println("--- Stored Resources: " + resourceNode.getStoredResources());
                System.out.println("--- Base Capacity: " + resourceNode.getBaseCapacity());
                System.out.println("--- Tier: " + resourceNode.getTier());
            }
        }
    }
}