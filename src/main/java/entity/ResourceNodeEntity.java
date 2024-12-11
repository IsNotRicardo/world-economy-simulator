package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resource_node")
public class ResourceNodeEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "country_id", nullable = false)
	private CountryEntity country;

	@ManyToOne
	@JoinColumn(name = "resource_id", nullable = false)
	private ResourceEntity resource;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@Column(name = "tier", nullable = false)
	private int tier;

	@Column(name = "base_capacity", nullable = false)
	private int baseCapacity;

	@Column(name = "base_production_cost", nullable = false)
	private double baseProductionCost;

	public ResourceNodeEntity() {
	}

	public ResourceNodeEntity(CountryEntity country,
	                          ResourceEntity resource,
	                          int quantity,
	                          int tier,
	                          int baseCapacity,
	                          double baseProductionCost) {
		this.country = country;
		this.resource = resource;
		this.quantity = quantity;
		this.baseCapacity = baseCapacity;
		this.tier = tier;
		this.baseProductionCost = baseProductionCost;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public CountryEntity getCountry() {
		return country;
	}

	public void setCountry(CountryEntity country) {
		this.country = country;
	}

	public ResourceEntity getResource() {
		return resource;
	}

	public void setResource(ResourceEntity resource) {
		this.resource = resource;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	public int getBaseCapacity() {
		return baseCapacity;
	}

	public void setBaseCapacity(int baseCapacity) {
		this.baseCapacity = baseCapacity;
	}

	public double getBaseProductionCost() {
		return baseProductionCost;
	}

	public void setBaseProductionCost(double baseProductionCost) {
		this.baseProductionCost = baseProductionCost;
	}

	@Override
	public String toString() {
		return "ResourceNodeEntity [" + "id=" + id + ", country=" + country + ", resource=" + resource + ", quantity=" +
		       quantity + ']';
	}
}