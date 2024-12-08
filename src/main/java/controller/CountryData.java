package controller;

public record CountryData(String name, long population, double money, int starterResources, int ownedResources) {
	public CountryData {
		if (population <= 0) {
			throw new IllegalArgumentException("Population must be positive.");
		}
		if (money < 0) {
			throw new IllegalArgumentException("Money cannot be negative.");
		}

	}
}
