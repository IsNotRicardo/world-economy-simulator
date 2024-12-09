package entity;

import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "country")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "name", unique = true, nullable = false)
	private String name;

	@Column(name = "money", nullable = false)
	private double money;

	@Column(name = "population", nullable = false)
	private Long population;

	@OneToMany(mappedBy = "country", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<CountryResource> countryResources;

	public Country() {
	}

	public Country(String name, double money, long population) {
		this.name = name;
		this.money = money;
		this.population = population;
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public long getPopulation() {
		return population;
	}

	public void setPopulation(long population) {
		this.population = population;
	}

	public Set<CountryResource> getCountryResources() {
		return countryResources;
	}

	public void setCountryResources(Set<CountryResource> countryResources) {
		this.countryResources = countryResources;
	}
}