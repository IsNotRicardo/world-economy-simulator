package model.simulation;

import model.core.Country;
import model.simulation.Clock;
import model.simulation.Event;
import model.simulation.EventType;

import java.util.PriorityQueue;
import java.util.Random;

public class ArrivalScheduler {
    private final PriorityQueue<Event> eventQueue;
    private final Clock clock;

    public ArrivalScheduler(PriorityQueue<Event> eventQueue) {
        this.eventQueue = eventQueue;
        this.clock = Clock.getInstance();
    }

    public void scheduleArrival(Country country, double lambda) {
        double nextArrivalTime = generateExponential(lambda);
        Event arrivalEvent = new Event(EventType.ARRIVAL, clock.getTime() + nextArrivalTime, country);
        eventQueue.add(arrivalEvent);
    }

    private double generateExponential(double lambda) {
        Random random = new Random();
        return Math.log(1 - random.nextDouble()) / (-lambda);
    }
}