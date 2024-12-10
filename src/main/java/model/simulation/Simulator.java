package model.simulation;

import dao.*;
import entity.*;
import controller.SimulationController;
import model.core.*;

import java.util.List;
import java.util.Map;

public class Simulator {
	private final Clock clock = Clock.getInstance();
	private final EventList eventList = new EventList();

	private final SimulationController simulationController;
	private final List<Resource> resources;
	private final List<Country> countries;

	public Simulator(SimulationController simulationController, List<Resource> resources, List<Country> countries) {
		this.simulationController = simulationController;
		this.resources = resources;
		this.countries = countries;
	}

	private final ResourceDao resourceDao = new ResourceDao();
	private final CountryDao countryDao = new CountryDao();
	private final CountryMetricsDao countryMetricsDao = new CountryMetricsDao();
	private final ResourceMetricsDao resourceMetricsDao = new ResourceMetricsDao();
	private final ResourceNodeMetricsDao resourceNodeMetricsDao = new ResourceNodeMetricsDao();

	public void runSimulation() {
		initializeSimulation();

		while (clock.getTime() < SimulationConfig.getSimulationTime()) {
			if (!clock.isPaused()) {
				// A-phase
				// Advance the clock to the next event time
				Event nextEvent = eventList.peekNextEvent();
				if (nextEvent != null) {
					clock.setTime(nextEvent.getTime());
				}

				// B-phase
				// Process all events that are scheduled to occur at the current time
				while (nextEvent != null && nextEvent.getTime() == clock.getTime()) {
					processEvent(eventList.getNextEvent());
					nextEvent = eventList.peekNextEvent();
				}

				// C-phase
				// Create new events based on specific conditions
				// Currently, there are no available conditions to check

                saveMetrics();
                updateController();
                System.out.println("\nDay " + clock.getTime() + " completed.");
            }
            try {
                Thread.sleep(SimulationConfig.getSimulationDelay());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                System.out.println("Simulation interrupted.");
            }
        }

		finalizeSimulation();
	}

	private void initializeSimulation() {
		// Since all current events happen daily, adding +1 to the current time is sufficient
		int nextEventTime = clock.getTime() + 1;

		eventList.addEvent(new Event(EventType.UPDATE_PEOPLE, nextEventTime));
		eventList.addEvent(new Event(EventType.OBTAIN_RESOURCES, nextEventTime));
		eventList.addEvent(new Event(EventType.SERVE_PEOPLE, nextEventTime));
		eventList.addEvent(new Event(EventType.REQUEST_RESOURCES, nextEventTime));

		System.out.println("Simulation initialized.");
	}

	private void processEvent(Event event) {
		int nextEventTime = clock.getTime() + 1;

		switch (event.getType()) {
			case UPDATE_PEOPLE:
				for (Country country : countries) {
					country.updatePeople();
				}
				eventList.addEvent(new Event(EventType.UPDATE_PEOPLE, nextEventTime));
				break;
			case OBTAIN_RESOURCES:
				for (Country country : countries) {
					country.obtainResources();
				}
				eventList.addEvent(new Event(EventType.OBTAIN_RESOURCES, nextEventTime));
				break;
			case SERVE_PEOPLE:
				for (Country country : countries) {
					country.servePeople();
				}
				eventList.addEvent(new Event(EventType.SERVE_PEOPLE, nextEventTime));
				break;
			case REQUEST_RESOURCES:
				for (Country country : countries) {
					country.requestResources();
				}
				eventList.addEvent(new Event(EventType.REQUEST_RESOURCES, nextEventTime));
				break;
			default:
				throw new IllegalArgumentException("Unknown event type: " + event.getType());
		}
	}

	private void saveMetrics() {

		for (Country country : countries) {
			System.out.println("\nSaving metrics for " + country.getName());

			CountryEntity countryEntity = countryDao.findByName(country.getName());
			if (countryEntity == null) {
				countryEntity = new CountryEntity(country.getName(), country.getMoney(), country.getPopulation());
				countryDao.persist(countryEntity);
			}

			double individualBudget = country.getSegmentBudget() / SimulationConfig.getPopulationSegmentSize();

			CountryMetricsEntity countryMetrics =
					new CountryMetricsEntity(clock.getTime(), countryEntity, country.getPopulation(),
					                         country.getMoney(), country.getAverageHappiness(), individualBudget);

			try {
				countryMetricsDao.persist(countryMetrics);
			} catch (Exception e) {
				System.out.println("Error persisting country metrics: " + e.getMessage());
			}

			if (country.getResourceStorage().isEmpty() && country.getResourceNodes().isEmpty()) {
				System.out.println("No resources or resource nodes to save metrics for.");
				continue;
			}

			for (Map.Entry<Resource, ResourceInfo> entry : country.getResourceStorage().entrySet()) {
				Resource resource = entry.getKey();
				ResourceInfo resourceInfo = entry.getValue();

				ResourceEntity resourceEntity = getResourceEntity(resource, null);

				ResourceMetricsEntity resourceMetrics =
						new ResourceMetricsEntity(clock.getTime(), countryEntity, resourceEntity,
						                          resourceInfo.getQuantity(), resourceInfo.getValue());
				try {
					resourceMetricsDao.persist(resourceMetrics);
				} catch (Exception e) {
					System.out.println("Error persisting resource metrics: " + e.getMessage());
				}
			}

			if (country.getResourceNodes().isEmpty()) {
				System.out.println("No resource nodes to save metrics for.");
				continue;
			}

			for (ResourceNode resourceNode : country.getResourceNodes()) {

				ResourceEntity resourceEntity = getResourceEntity(null, resourceNode);

				ResourceNodeMetricsEntity resourceNodeMetrics =
						new ResourceNodeMetricsEntity(clock.getTime(), countryEntity, resourceEntity,
						                              resourceNode.getMaxCapacity(), resourceNode.getProductionCost(),
						                              resourceNode.getTier());

				try {
					resourceNodeMetricsDao.persist(resourceNodeMetrics);
				} catch (Exception e) {
					System.out.println("Error persisting resource node metrics: " + e.getMessage());
				}
			}
		}
	}

    private void updateController() {
        simulationController.updateData();
    }

    private void finalizeSimulation() {
        // Generate a report or summary of the simulation results
        generateReport();

		System.out.println("\nSimulation finalized.");
	}

	private void generateReport() {
		// Implement logic to generate a report or summary of the simulation results
		System.out.println("\nGenerating simulation report...");
		// Example: Print summary statistics
		for (Country country : countries) {
			System.out.println("Country: " + country.getName());
			System.out.println("Population: " + country.getPopulation());
			System.out.println("Money: " + country.getMoney());

			for (Map.Entry<Resource, ResourceInfo> entry : country.getResourceStorage().entrySet()) {
				Resource resource = entry.getKey();
				ResourceInfo resourceInfo = entry.getValue();
				System.out.println("Resource: " + resource.name());
				System.out.println("--- Quantity: " + resourceInfo.getQuantity());
				System.out.println("--- Value: " + resourceInfo.getValue());
			}

			for (ResourceNode resourceNode : country.getResourceNodes()) {
				System.out.println("Resource Node: " + resourceNode.getResource().name());
				System.out.println("--- Stored Resources: " + resourceNode.getStoredResources());
				System.out.println("--- Base Capacity: " + resourceNode.getBaseCapacity());
				System.out.println("--- Tier: " + resourceNode.getTier());
			}
		}
	}

	private ResourceEntity getResourceEntity(Resource resource, ResourceNode resourceNode) {
		String resourceName;
		double priority;
		int baseCapacity;
		double productionCost;

		if (resource != null) {
			resourceName = resource.name();
			priority = resource.priority();
			baseCapacity = resource.baseCapacity();
			productionCost = resource.productionCost();
		} else if (resourceNode != null) {
			resourceName = resourceNode.getResource().name();
			priority = resourceNode.getResource().priority();
			baseCapacity = resourceNode.getResource().baseCapacity();
			productionCost = resourceNode.getResource().productionCost();
		} else {
			throw new IllegalArgumentException("Resource or ResourceNode must be provided.");
		}

		ResourceEntity resourceEntity = resourceDao.findByName(resourceName);
		if (resourceEntity == null) {
			resourceEntity = new ResourceEntity(resourceName, priority, baseCapacity, productionCost);
			try {
				resourceDao.persist(resourceEntity);
			} catch (Exception e) {
				System.out.println("Error persisting resource: " + e.getMessage());
			}
		}
		return resourceEntity;
	}
}