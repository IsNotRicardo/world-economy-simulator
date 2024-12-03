package model.simulation;

import model.core.Country;

public class Event implements Comparable<Event> {
    private final EventType type;
    private final double time;

    public Event(EventType type, double time, Country country) {
        this.type = type;
        this.time = time;
    }

    public EventType getType() {
        return type;
    }

    public double getTime() {
        return time;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}