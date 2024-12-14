package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "resource_metrics")
public class ResourceMetricsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "day", nullable = false)
	private int day;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "country_id", nullable = false)
	private CountryEntity country;

	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "resource_id", nullable = false)
	private ResourceEntity resource;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	@Column(name = "value", nullable = false)
	private double value;

	public ResourceMetricsEntity() {
	}

	public ResourceMetricsEntity(int day, CountryEntity country, ResourceEntity resource, int quantity, double value) {
		this.day = day;
		this.resource = resource;
		this.country = country;
		this.quantity = quantity;
		this.value = value;
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

	public ResourceEntity getResource() {
		return resource;
	}

	public void setResource(ResourceEntity resource) {
		this.resource = resource;
	}

	public CountryEntity getCountry() {
		return country;
	}

	public void setCountry(CountryEntity country) {
		this.country = country;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public double getValue() {
		return value;
	}

	public void setValue(double value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "ResourceMetricsEntity {" + "id=" + id + ", day=" + day + ", resource=" + resource + ", country=" +
		       country + ", quantity=" + quantity + ", value=" + value + '}';
	}

}
