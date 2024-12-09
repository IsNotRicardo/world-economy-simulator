package entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "resource")
public class Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "priority", nullable = false)
	private double priority;

	@Column(name = "base_capacity", nullable = false)
	private int baseCapacity;

	@Column(name = "production_cost", nullable = false)
	private double productionCost;

	@OneToMany(mappedBy = "resource", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CountryResource> countryResources;

	public Resource() {
	}

	public Resource(String name, double priority, int baseCapacity, double productionCost) {
		this.name = name;
		this.priority = priority;
		this.baseCapacity = baseCapacity;
		this.productionCost = productionCost;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPriority() {
		return priority;
	}

	public void setPriority(double priority) {
		this.priority = priority;
	}

	public int getBaseCapacity() {
		return baseCapacity;
	}

	public void setBaseCapacity(int baseCapacity) {
		this.baseCapacity = baseCapacity;
	}

	public double getProductionCost() {
		return productionCost;
	}

	public void setProductionCost(double productionCost) {
		this.productionCost = productionCost;
	}

	public Set<CountryResource> getCountryResources() {
		return countryResources;
	}

	public void setCountryResources(Set<CountryResource> countryResources) {
		this.countryResources = countryResources;
	}
}