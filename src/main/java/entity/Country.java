package entity;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "country")
public class Country {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "name", nullable = false, unique = true)
	private String name;

	@Column(name = "money", nullable = false)
	private double money;

	@Column(name = "population", nullable = false)
	private int population;

	@OneToMany(mappedBy = "country")
	private Set<CountryResource> countryResources;

	public Country() {
	}

	public Country(String name, double money, int population) {
		this.name = name;
		this.money = money;
		this.population = population;
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

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public int getPopulation() {
		return population;
	}

	public void setPopulation(int population) {
		this.population = population;
	}
}