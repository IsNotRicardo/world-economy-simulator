package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "country_resource")
public class CountryResource {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "country_id", nullable = false)
	private Country country;

	@ManyToOne
	@JoinColumn(name = "resource_id", nullable = false)
	private Resource resource;

	@Column(name = "quantity", nullable = false)
	private int quantity;

	public CountryResource() {
	}

	public CountryResource(Country country, Resource resource, int quantity) {
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

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public Resource getResource() {
		return resource;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "CountryResource [id=" + id + ", country=" + country + ", resource=" + resource + ", quantity=" + quantity
				+ "]";
	}
}
