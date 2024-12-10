package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "country_resource")
public class CountryResource {

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

	public CountryResource() {
	}

	public CountryResource(CountryEntity country, ResourceEntity resource, int quantity) {
		this.country = country;
		this.resource = resource;
		this.quantity = quantity;
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
}