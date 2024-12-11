package entity;

import java.io.Serializable;
import java.util.Objects;

public class ResourceNodeEntityId implements Serializable {
	private Long countryId;
	private Long resourceId;

	public ResourceNodeEntityId() {
	}

	public ResourceNodeEntityId(Long countryId, Long resourceId) {
		this.countryId = countryId;
		this.resourceId = resourceId;
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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ResourceNodeEntityId that = (ResourceNodeEntityId) o;
		return Objects.equals(countryId, that.countryId) &&
		       Objects.equals(resourceId, that.resourceId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(countryId, resourceId);
	}
}