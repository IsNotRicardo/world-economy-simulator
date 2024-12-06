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
        clock.start();

        while (clock.getTime() < SimulationConfig.getSimulationTime()) {
            // A-phase
            // Advance the clock to the next event time
            Event nextEvent = eventList.getNextEvent();
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
            // Create new events based on specific conditions

            // Currently, there are no available conditions to check
        }

        clock.stop();
        obtainResults();
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

    private void obtainResults() {
        // Obtain and process the results of the simulation
    }
}

