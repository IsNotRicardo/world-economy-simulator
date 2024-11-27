package model;

import java.util.PriorityQueue;
import java.util.Random;

public class ArrivalScheduler {
    private final PriorityQueue<Event> eventQueue;
    private double currentTime;

    public ArrivalScheduler(PriorityQueue<Event> eventQueue, double currentTime) {
        this.eventQueue = eventQueue;
        this.currentTime = currentTime;
    }

    public void scheduleArrival(Country country, double lambda) {
        double nextArrivalTime = generateExponential(lambda);
        Event arrivalEvent = new Event(EventType.ARRIVAL, currentTime + nextArrivalTime, country);
        eventQueue.add(arrivalEvent);
    }

    private double generateExponential(double lambda) {
        Random random = new Random();
        return Math.log(1 - random.nextDouble()) / (-lambda);
    }
}