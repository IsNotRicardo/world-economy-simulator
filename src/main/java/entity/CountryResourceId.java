package entity;

import java.io.Serializable;

public class CountryResourceId implements Serializable {

	private long country;
	private long resource;

	public CountryResourceId() {
	}

	public CountryResourceId(long country, long resource) {
		this.country = country;
		this.resource = resource;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null || getClass() != obj.getClass()) {
			return false;
		}
		CountryResourceId other = (CountryResourceId) obj;
		return country == other.country && resource == other.resource;
	}

	@Override
	public int hashCode() {
		return Long.hashCode(country) + Long.hashCode(resource);
	}
}