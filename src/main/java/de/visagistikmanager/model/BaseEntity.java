package de.visagistikmanager.model;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import org.hibernate.annotations.GenericGenerator;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class BaseEntity implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "gen", strategy = "increment")
	@GeneratedValue(generator = "gen")
	private Long id;

	public boolean isNew() {
		return this.id == null;
	}

	@Override
	public boolean equals(final Object object) {
		if (this == object) {
			return true;
		}
		if (object == null) {
			return false;
		}
		if (getClass() != object.getClass()) {
			return false;
		}

		final BaseEntity other = (BaseEntity) object;
		if (this.getId() != other.getId() && (this.getId() == null || !id.equals(other.id))) {
			return false;
		}
		return true;
	}

}
