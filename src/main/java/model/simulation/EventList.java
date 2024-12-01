package model.simulation;

import java.util.PriorityQueue;

public class EventList {
    private final PriorityQueue<Event> eventQueue = new PriorityQueue<>();

    public void addEvent(Event event) {
        eventQueue.add(event);
    }

    public Event getNextEvent() {
        return eventQueue.poll();
    }

    public boolean hasMoreEvents() {
        return !eventQueue.isEmpty();
    }

    public Event peekNextEvent() {
        return eventQueue.peek();
    }

    public PriorityQueue<Event> getEventQueue() {
        return eventQueue;
    }
}