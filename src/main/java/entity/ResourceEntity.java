package entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "resource")
public class ResourceEntity {

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
	private Set<ResourceNodeEntity> resourceNodeEntities;

	public ResourceEntity() {
	}

	public ResourceEntity(String name, double priority, int baseCapacity, double productionCost) {
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

	public Set<ResourceNodeEntity> getResourceNodeEntities() {
		return resourceNodeEntities;
	}

	public void setResourceNodeEntities(Set<ResourceNodeEntity> resourceNodeEntities) {
		this.resourceNodeEntities = resourceNodeEntities;
	}

	@Override
	public String toString() {
		return "ResourceEntity [" + "id=" + id + ", name='" + name + '\'' + ", priority=" + priority +
		       ", baseCapacity=" + baseCapacity + ", productionCost=" + productionCost + ']';
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		ResourceEntity that = (ResourceEntity) obj;
		return name != null && name.equals(that.name);
	}

	@Override
	public int hashCode() {
		return name != null ? name.hashCode() : 0;
	}
}