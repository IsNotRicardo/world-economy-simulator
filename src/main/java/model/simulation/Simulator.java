package model.simulation;

import model.core.Country;
import model.core.Resource;

import java.util.List;

public class Simulator {
    private final Clock clock = Clock.getInstance();
    private final EventList eventList = new EventList();

    private final List<Resource> resources;
    private final List<Country> countries;

    public Simulator(List<Resource> resources, List<Country> countries) {
        this.resources = resources;
        this.countries = countries;
    }

    public void runSimulation() {
        initializeSimulation();
        clock.start(); // Start the clock

        while (clock.getTime() < SimulationConfig.getSimulationTime()) {
            // A-phase
            // Advance the clock to the next event time
            while (true) {
                Event nextEvent = eventList.peekNextEvent();
                if (nextEvent == null || nextEvent.getTime() > clock.getTime()) {
                    break;
                }
                // B-phase
                // Process all events that are scheduled to occur at the current time
                processEvent(nextEvent);
                eventList.getNextEvent(); // Remove the processed event
            }

            // C-phase
            // Process any events that are triggered by the events in the B-phase
        }

        clock.stop(); // Stop the clock when the simulation ends
        obtainResults();
    }

    private void initializeSimulation() {
        // Initialize the simulation (e.g., schedule initial events)
    }

    private void processEvent(Event event) {
        // Implement event processing logic
    }

    private void obtainResults() {
        // Obtain and process the results of the simulation
    }
}