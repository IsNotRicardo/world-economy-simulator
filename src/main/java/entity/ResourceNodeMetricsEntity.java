package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resource_node_metrics")
public class ResourceNodeMetricsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "day", nullable = false)
	private int day;

	@ManyToOne
	@JoinColumn(name = "country_id", nullable = false)
	private CountryEntity country;

	@ManyToOne
	@JoinColumn(name = "resource_id", nullable = false)
	private ResourceEntity resource;

	@Column(name = "max_capacity", nullable = false)
	private int maxCapacity;

	@Column(name = "production_cost", nullable = false)
	private double productionCost;

	@Column(name = "tier", nullable = false)
	private int tier;

	public ResourceNodeMetricsEntity() {
	}

	public ResourceNodeMetricsEntity(int day, CountryEntity country, ResourceEntity resource, int maxCapacity,
	                                 double productionCost, int tier) {
		this.day = day;
		this.country = country;
		this.resource = resource;
		this.maxCapacity = maxCapacity;
		this.productionCost = productionCost;
		this.tier = tier;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public int getDay() {
		return day;
	}

	public void setDay(int day) {
		this.day = day;
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

	public int getMaxCapacity() {
		return maxCapacity;
	}

	public void setMaxCapacity(int maxCapacity) {
		this.maxCapacity = maxCapacity;
	}

	public double getProductionCost() {
		return productionCost;
	}

	public void setProductionCost(double productionCost) {
		this.productionCost = productionCost;
	}

	public int getTier() {
		return tier;
	}

	public void setTier(int tier) {
		this.tier = tier;
	}

	@Override
	public String toString() {
		return "ResourceNodeMetricsEntity {" + "id=" + id + ", day=" + day + ", country=" + country + ", resource=" +
		       resource + ", maxCapacity=" + maxCapacity + ", productionCost=" + productionCost + ", tier=" + tier +
		       '}';
	}


}
