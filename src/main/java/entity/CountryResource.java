package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "country_resource")
@IdClass(CountryResourceId.class)
public class CountryResource {

	@Id
	@ManyToOne
	@JoinColumn(name = "country_id", referencedColumnName = "id")
	private Country country;

	@Id
	@ManyToOne
	@JoinColumn(name = "resource_id", referencedColumnName = "id")
	private Resource resource;

	public void setCountry(Country country) {
		this.country = country;
	}

	public void setResource(Resource resource) {
		this.resource = resource;
	}

	public Country getCountry() {
		return country;
	}

	public Resource getResource() {
		return resource;
	}
}