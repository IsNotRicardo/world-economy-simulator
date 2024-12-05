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

    private void runSimulation() {
        initializeSimulation();
        clock.start(); // Start the clock

        do {
            // A-phase
            // Advance the clock to the next event time
            Event nextEvent = eventList.peekNextEvent();
            if (nextEvent != null) {
                clock.setTime(nextEvent.getTime());
            }

            // B-phase
            // Process all events that are scheduled to occur at the current time
            while (true) {
                nextEvent = eventList.peekNextEvent();
                if (nextEvent == null || nextEvent.getTime() > clock.getTime()) {
                    break;
                }
                processEvent(nextEvent);
                eventList.getNextEvent(); // Remove the processed event
            }

            // C-phase
            // Process any events that are triggered by the events in the B-phase
            checkAndAddNewEvents();
        } while (clock.getTime() < SimulationConfig.getSimulationTime());

        clock.stop(); // Stop the clock when the simulation ends
        obtainResults();
    }

    private void initializeSimulation() {
        // Initialize the simulation (e.g., schedule initial events)
        // need to create four events types to be able to start the simulation and add it to the event list
        Event obtainResourcesEvent = new Event(EventType.OBTAIN_RESOURCES, clock.getTime(), null);
        Event servePeopleEvent = new Event(EventType.SERVE_PEOPLE, clock.getTime(), null);
        Event updatePeopleEvent = new Event(EventType.UPDATE_PEOPLE, clock.getTime(), null);
        Event requestResourcesEvent = new Event(EventType.REQUEST_RESOURCES, clock.getTime(), null);

        eventList.addEvent(obtainResourcesEvent);
        eventList.addEvent(servePeopleEvent);
        eventList.addEvent(updatePeopleEvent);
        eventList.addEvent(requestResourcesEvent);
    }

    private void processEvent(Event event) {
        for (Country country : countries) {
            switch (event.getType()) {
                case OBTAIN_RESOURCES:
                    country.obtainResources();
                    break;
                case SERVE_PEOPLE:
                    country.servePeople();
                    break;
                case UPDATE_PEOPLE:
                    country.updatePeople();
                    break;
                case REQUEST_RESOURCES:
                    country.requestResources();
                    break;
                default:
                    throw new IllegalArgumentException("Unknown event type: " + event.getType());
            }
        }
    }

    private void obtainResults() {
        // Obtain and process the results of the simulation
    }

    private void checkAndAddNewEvents() {
        for (EventType eventType : EventType.values()) {
            boolean eventExists = eventList.getEventQueue().stream()
                    .anyMatch(event -> event.getType() == eventType);

            if (!eventExists) {
                Event newEvent = new Event(eventType, clock.getTime(), null);
                eventList.addEvent(newEvent);
            }
        }
    }
}

