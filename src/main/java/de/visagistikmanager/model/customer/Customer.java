package de.visagistikmanager.model.customer;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import de.visagistikmanager.model.BaseEntity;
import de.visagistikmanager.model.order.Order;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@NamedQueries(value = {
		@NamedQuery(name = Customer.FIND_BY_SURNAME_AND_LASTNAME, query = "SELECT c from Customer c WHERE c.surname = :surname AND c.forename = :forename"),
		@NamedQuery(name = Customer.FIND_CUSTOMER_WITH_BIRTHDAYS, query = "SELECT c from Customer c WHERE c.birthday is not null") })
public class Customer extends BaseEntity {

	private static final long serialVersionUID = 1L;

	public static final String FIND_BY_SURNAME_AND_LASTNAME = "findBySurnameAndLastName";

	public static final String FIND_CUSTOMER_WITH_BIRTHDAYS = "findCustomerWithBirthdays";

	private String surname;

	private String forename;

	private LocalDate birthday;

	private String street;

	private String streetNumber;

	private Integer zip;

	private String city;

	private boolean blemishes;

	private String allergies;

	@ElementCollection
	private Set<String> skinFeatures = new HashSet<>();

	@ElementCollection
	private Set<String> sensitivities = new HashSet<>();

	@ElementCollection
	private Set<String> improvments = new HashSet<>();

	@ElementCollection
	private Set<String> currentSkinCare = new HashSet<>();

	@ElementCollection
	private Set<String> interests = new HashSet<>();

	@ElementCollection
	private Set<String> contactOptions = new HashSet<>();

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "CustomerOrders")
	private Set<Order> orders;

	public String getAdress() {
		return this.street + " " + this.streetNumber + "\n" + this.zip + " " + this.city;
	}

	public String getFullNameInverse() {
		return this.surname + ", " + this.forename;
	}

	public String getFullNameForFiles() {
		return this.surname + this.forename;
	}

	public String getFullName() {
		return this.forename + " " + this.surname;
	}

	@Override
	public String toString() {
		return getFullNameInverse();
	}

}
