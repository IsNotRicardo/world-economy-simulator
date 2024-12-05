package model.simulation;

public class Event implements Comparable<Event> {
    private final EventType type;
    private final int time;

    public Event(EventType type, int time) {
        this.type = type;
        this.time = time;
    }

    public EventType getType() {
        return type;
    }

    public int getTime() {
        return time;
    }

    @Override
    public int compareTo(Event other) {
        return Double.compare(this.time, other.time);
    }
}