package model.simulation;

import model.core.Country;
import model.core.Resource;
import model.core.ResourceInfo;
import model.core.ResourceNode;

import java.util.List;
import java.util.Map;

public class Simulator {
    private final Clock clock = Clock.getInstance();
    private final EventList eventList = new EventList();

    private final List<Resource> resources;
    private final List<Country> countries;

    public Simulator(List<Resource> resources, List<Country> countries) {
        this.resources = resources;
        this.countries = countries;

        for (Country country : countries) {
            country.addAllCountries(countries);
        }
    }

    public void runSimulation() {
        initializeSimulation();
        clock.start();

        while (clock.isRunning() && clock.getTime() < SimulationConfig.getSimulationTime()) {
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
                    processEvent(nextEvent);
                    nextEvent = eventList.getNextEvent();
                }

                // C-phase
                // Process any events that are triggered by the events in the B-phase
                checkAndAddNewEvents();
            }

            // Simulate delay for each day
            try {
                Thread.sleep(SimulationConfig.getSimulationDelay() * 1000);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted state
                System.out.println("Simulation interrupted.");
            }
        }

        clock.stop();
        finalizeSimulation();
    }

    private void initializeSimulation() {
        // Since all current events happen daily, adding +1 to the current time is sufficient
        int nextEventTime = clock.getTime() + 1;

        eventList.addEvent(new Event(EventType.UPDATE_PEOPLE, nextEventTime));
        eventList.addEvent(new Event(EventType.OBTAIN_RESOURCES, nextEventTime));
        eventList.addEvent(new Event(EventType.SERVE_PEOPLE, nextEventTime));
        eventList.addEvent(new Event(EventType.REQUEST_RESOURCES, nextEventTime));
    }

    private void processEvent(Event event) {
        int nextEventTime = clock.getTime() + 1;

        for (Country country : countries) {
            switch (event.getType()) {
                case UPDATE_PEOPLE:
                    country.updatePeople();
                    eventList.addEvent(new Event(EventType.UPDATE_PEOPLE, nextEventTime));
                    break;
                case OBTAIN_RESOURCES:
                    country.obtainResources();
                    eventList.addEvent(new Event(EventType.OBTAIN_RESOURCES, nextEventTime));
                    break;
                case SERVE_PEOPLE:
                    country.servePeople();
                    eventList.addEvent(new Event(EventType.SERVE_PEOPLE, nextEventTime));
                    break;
                case REQUEST_RESOURCES:
                    country.requestResources();
                    eventList.addEvent(new Event(EventType.REQUEST_RESOURCES, nextEventTime));
                    break;
                default:
                    throw new IllegalArgumentException("Unknown event type: " + event.getType());
            }
        }
    }

    private void checkAndAddNewEvents() {
        // Implement logic to check conditions and add new events if necessary
    }

    private void saveMetrics() {
        for (Country country : countries) {
            // Save the country metrics
            // Call the DAO here

            // Save the resource metrics per country
            for (Map.Entry<Resource, ResourceInfo> entry : country.getResourceStorage().entrySet()) {
                // Call the DAO here
            }

            // Save the resource node metrics per country
            for (ResourceNode resourceNode : country.getResourceNodes()) {
                // Call the DAO here
            }
        }
    }

    private void finalizeSimulation() {
        // Save the metrics for each country
        saveMetrics();

        // Generate a report or summary of the simulation results
        generateReport();

        System.out.println("Simulation finalized. Results have been saved.");
    }

    private void generateReport() {
        // Implement logic to generate a report or summary of the simulation results
        System.out.println("Generating simulation report...");
        // Example: Print summary statistics
        for (Country country : countries) {
            System.out.println("Country: " + country.getName());
            System.out.println("Population: " + country.getPopulation());
            System.out.println("Resources: " + country.getResourceStorage());
        }
    }

}