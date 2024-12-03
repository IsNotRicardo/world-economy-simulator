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

        while (clock.getTime() < SimulationConfig.getSimulationTime()) {
            // A-phase
            // Advance the clock to the next event time

            // B-phase
            // Process all events that are scheduled to occur at the current time

            // C-phase
            // Process any events that are triggered by the events in the B-phase
        }

        obtainResults();
    }

    private void initializeSimulation() {

    }

    private void obtainResults() {

    }
}
