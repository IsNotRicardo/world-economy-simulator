package model;

import java.util.ArrayList;
import java.util.List;

public class Country {
    private final List<ResourceNode> resourceNodes = new ArrayList<>();
    private final List<Person> peopleObjects = new ArrayList<>();

    private final String name;
    private long population;

    public Country(String name, int population) {
        this.name = name;
        this.population = population;
    }
}
