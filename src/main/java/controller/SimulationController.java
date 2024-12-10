package controller;

import model.simulation.Event;
import model.simulation.EventList;
import model.simulation.SimulationConfig;
import model.simulation.Simulator;

public class SimulationController {
    private final EventList eventList;
//    private final Simulator simulator;
    private boolean isRunning;

    public SimulationController(SimulationConfig config) {
        this.eventList = new EventList();
//        this.simulator = new Simulator(config, eventList);
        this.isRunning = false;
    }

    public void startSimulation() {
        isRunning = true;
        while (isRunning && eventList.hasMoreEvents()) {
            Event event = eventList.getNextEvent();
            processEvent(event);
        }
    }

    public void pauseSimulation() {
        isRunning = false;
    }

    public void resumeSimulation() {
        isRunning = true;
        startSimulation();
    }

    public void stopSimulation() {
        isRunning = false;
        eventList.getEventQueue().clear();
    }

    private void processEvent(Event event) {
        switch (event.getType()) {
            case OBTAIN_RESOURCES:
                handleObtainResourcesEvent(event);
                break;
            case SERVE_PEOPLE:
                handleServePeopleEvent(event);
                break;
            case REQUEST_RESOURCES:
                handleRequestResourcesEvent(event);
                break;
            default:
                throw new IllegalArgumentException("Unknown event type: " + event.getType());
        }
    }

    private void handleObtainResourcesEvent(Event event) {
        // Implement obtain resources event handling logic
    }

    private void handleServePeopleEvent(Event event) {
        // Implement serve people event handling logic
    }

    private void handleRequestResourcesEvent(Event event) {
        // Implement request resources event handling logic
    }
}

// the controller should get the simulation configuration from the view, bacically the user input and pass it to the model