package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resource_node")
@IdClass(ResourceNodeEntityId.class)
public class ResourceNodeEntity {

	@Id
	@Column(name = "country_id", nullable = false)
	private Long countryId;

	@Id
	@Column(name = "resource_id", nullable = false)
	private Long resourceId;

	@ManyToOne
	@JoinColumn(name = "country_id", insertable = false, updatable = false, nullable = false)
	private CountryEntity country;

	@ManyToOne
	@JoinColumn(name = "resource_id", insertable = false, updatable = false, nullable = false)
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

	public ResourceNodeEntity(CountryEntity country, ResourceEntity resource, int quantity, int tier, int baseCapacity,
	                          double baseProductionCost) {
		this.country = country;
		this.resource = resource;
		if (country != null) {
			this.countryId = country.getId();
		}
		if (resource != null) {
			this.resourceId = resource.getId();
		}
		this.quantity = quantity;
		this.baseCapacity = baseCapacity;
		this.tier = tier;
		this.baseProductionCost = baseProductionCost;
	}

	public Long getCountryId() {
		return countryId;
	}

	public void setCountryId(Long countryId) {
		this.countryId = countryId;
	}

	public Long getResourceId() {
		return resourceId;
	}

	public void setResourceId(Long resourceId) {
		this.resourceId = resourceId;
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
		return "ResourceNodeEntity{" +
		       "countryId=" + countryId +
		       ", resourceId=" + resourceId +
		       ", quantity=" + quantity +
		       ", tier=" + tier +
		       ", baseCapacity=" + baseCapacity +
		       ", baseProductionCost=" + baseProductionCost +
		       '}';
	}
}