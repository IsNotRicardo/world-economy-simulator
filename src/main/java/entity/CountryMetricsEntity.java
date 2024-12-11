package entity;

import jakarta.persistence.*;

@Entity
@Table(name = "country_metrics")
public class CountryMetricsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "day", nullable = false)
	private int day;

	@ManyToOne
	@JoinColumn(name = "country_id", nullable = false)
	private CountryEntity country;

	@Column(name = "population", nullable = false)
	private long population;

	@Column(name = "money", nullable = false)
	private double money;

	@Column(name = "average_happiness", nullable = false)
	private double averageHappiness;

	@Column(name = "individual_budget", nullable = false)
	private double individualBudget;

	public CountryMetricsEntity() {
	}

	public CountryMetricsEntity(int day, CountryEntity country, long population, double money, double averageHappiness,
	                            double individualBudget) {
		this.day = day;
		this.country = country;
		this.population = population;
		this.money = money;
		this.averageHappiness = averageHappiness;
		this.individualBudget = individualBudget;
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

	public long getPopulation() {
		return population;
	}

	public void setPopulation(long population) {
		this.population = population;
	}

	public double getMoney() {
		return money;
	}

	public void setMoney(double money) {
		this.money = money;
	}

	public double getAverageHappiness() {
		return averageHappiness;
	}

	public void setAverageHappiness(double averageHappiness) {
		this.averageHappiness = averageHappiness;
	}

	public double getIndividualBudget() {
		return individualBudget;
	}

	public void setIndividualBudget(double individualBudget) {
		this.individualBudget = individualBudget;
	}

	@Override
	public String toString() {
		return "CountryMetricsEntity {" + "id=" + id + ", country=" + country + ", population=" + population +
		       ", money=" + money + ", averageHappiness=" + averageHappiness + ", individualBudget=" +
		       individualBudget + '}';
	}
}
