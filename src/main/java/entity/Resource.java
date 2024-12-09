package entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "resource")
public class Resource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "priority", nullable = false)
	private double priority;

	@Column(name = "base_capacity", nullable = false)
	private int baseCapacity;

	@Column(name = "production_cost", nullable = false)
	private double productionCost;

	@OneToMany(mappedBy = "resource")
	private Set<CountryResource> countryResources;

	public Resource() {
	}

	public Resource(String name, double priority, int baseCapacity, double productionCost) {
		this.name = name;
		this.priority = priority;
		this.baseCapacity = baseCapacity;
		this.productionCost = productionCost;
	}

	public long getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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

	public double getPriority() {
		return priority;
	}

	public void setPriority(double priority) {
		this.priority = priority;
	}
}