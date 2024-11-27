package controller;

import model.Country;
import java.util.List;
import model.ResourceNode;
import model.Resource;

public class InitialController {
    private final List<Country> countries;

    public InitialController(List<Country> countries) {
        this.countries = countries;
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void runSimulation(int timeSteps) {
        for (int i = 0; i < timeSteps; i++) {
            for (Country country : countries) {
                // Simulate resource allocation
                country.allocateResources();

                // Simulate trade between countries
                for (Country otherCountry : countries) {
                    if (!country.equals(otherCountry)) {
                        // Example trade logic (this should be expanded based on your requirements)
                        for (ResourceNode resourceNode : country.getResourceNodes()) {
                            Resource resource = new Resource(resourceNode.getType(), 0); // Assuming price is 0 for simplicity
                            country.trade(otherCountry, resource);
                        }
                    }
                }
            }
        }
    }
}