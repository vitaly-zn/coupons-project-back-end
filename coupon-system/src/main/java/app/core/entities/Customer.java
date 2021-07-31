package app.core.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;

import app.core.exceptions.CouponSystemException;

/**
 * The class {@code Customer} is the bean with all available functionality for
 * coupon system application.
 * 
 * @author Vitaly Zlobin
 *
 */
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Customer {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "first_name")
	private String firstName;
	@Column(name = "last_name")
	private String lastName;
	private String email;
	private String password;

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.DETACH, CascadeType.REFRESH }, fetch = FetchType.LAZY)
	@JoinTable(name = "customers_vs_coupons", joinColumns = @JoinColumn(name = "customer_id"), inverseJoinColumns = @JoinColumn(name = "coupon_id"))
	private List<Coupon> coupons;

	{
		this.coupons = new ArrayList<>();
	}

	/**
	 * Initializes new customer object with hard coded values (validated correct
	 * values).
	 * 
	 * @throws CouponSystemException if some validation was failed.
	 */
	public Customer() throws CouponSystemException {
		this(0, "DefaultCustomerFirstName", "DefaultCustomerLastName", "DefaultCustomerEmail",
				"DefaultCustomerPassword");
	}

	/**
	 * Initializes new customer object only if specified values are passed all
	 * validations.
	 * 
	 * @param id        specified customer id.
	 * @param firstName specified customer first name.
	 * @param lastName  specified customer last name.
	 * @param email     specified customer email.
	 * @param password  specified customer password.
	 * @throws CouponSystemException if some validation was failed.
	 */
	public Customer(int id, String firstName, String lastName, String email, String password)
			throws CouponSystemException {
		setId(id);
		setFirstName(firstName);
		setLastName(lastName);
		setEmail(email);
		setPassword(password);
	}

	/**
	 * Adds specified coupon to this customer coupons purchases list only if
	 * specified value not {@code null}.
	 * 
	 * @param coupon specified coupon to add.
	 * @throws CouponSystemException if specified coupon is {@code null}.
	 */
	public void addCoupon(Coupon coupon) throws CouponSystemException {
		if (coupon == null)
			throw new CouponSystemException("Customer failed to add new coupon: coupon is empty!");
		if (coupons != null) {
			int index = coupons.indexOf(coupon);
			if (index != -1)
				throw new CouponSystemException("Customer failed to add new coupon: already axists!");
		} else
			coupons = new ArrayList<>();
		coupons.add(coupon);
	}

	@PreRemove
	public void deleteCustomersFromCoupons() {
		for (Coupon c : coupons) {
			c.getCustomers().remove(this);
		}
	}

	/**
	 * Removes coupon from this customer coupons purchases list by specified coupon
	 * id, if exists.
	 * 
	 * @param id specified coupon id.
	 * @throws CouponSystemException if specified coupon id is negative.
	 */
	public void deleteCoupon(int id) throws CouponSystemException {
		if (id < 0)
			throw new CouponSystemException("Customer failed to remove coupon by id: value is negative!");
		Coupon coupon = new Coupon();
		coupon.setId(id);
		int index = coupons.indexOf(coupon);
		if (index == -1)
			throw new CouponSystemException("Customer failed to remove coupon by id: not found!");

		coupon = coupons.get(index);

		// remove all associations
		for (Coupon c : coupons) {
			c.getCustomers().remove(this);
		}
		coupons.remove(coupon);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Customer other = (Customer) obj;
		return id == other.id;
	}

	/**
	 * Returns {@link List}<{@link Coupon}> that represents list of this customer
	 * coupons purchases.
	 * 
	 * @return list of this customer coupons purchases.
	 */
	public List<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * Returns {@link String} that represents email of this customer.
	 * 
	 * @return email of this customer.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns {@link String} that represents first name of this customer.
	 * 
	 * @return first name of this customer.
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * Returns integer that represents id of this customer.
	 * 
	 * @return id of this customer.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns {@link String} that represents last name of this customer.
	 * 
	 * @return last name of this customer.
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * Returns {@link String} that represents password of this customer.
	 * 
	 * @return password of this customer.
	 */
	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Prints all customer coupons purchases to console.
	 */
	public void printAllCoupons() {
		if (coupons.isEmpty())
			System.out.println(this.firstName + " don't have coupons to display");
		else {
			int count = 1;
			System.out.println("List of " + this.firstName + "'s coupons:");
			for (Coupon coupon : coupons) {
				System.out.println("   " + count++ + ") " + coupon);
			}
		}
	}

	/**
	 * Sets customer coupons purchases list to specified only if it is not
	 * {@code null}.
	 * 
	 * @param coupons specified coupons purchases list to set.
	 */
	public void setCoupons(List<Coupon> coupons) {
		if (coupons != null)
			this.coupons = coupons;
	}

	/**
	 * Updates email of the current customer only if specified email value not
	 * {@code null}, not empty and not blank.
	 * 
	 * @param email specified email to update.
	 * @throws CouponSystemException if specified email value is {@code null}, empty
	 *                               or blank.
	 */
	public void setEmail(String email) throws CouponSystemException {
		if (email == null || email.isEmpty() || email.isBlank())
			throw new CouponSystemException("Failed to set customer email: text is empty!");
		this.email = email;
	}

	/**
	 * Updates first name of the current customer only if specified value not
	 * {@code null}, not empty and not blank.
	 * 
	 * @param firstName specified first name to update.
	 * @throws CouponSystemException if specified first name value is {@code null},
	 *                               empty or blank.
	 */
	public void setFirstName(String firstName) throws CouponSystemException {
		if (firstName == null || firstName.isEmpty() || firstName.isBlank())
			throw new CouponSystemException("Failed to set customer first name: text is empty!");
		this.firstName = firstName;
	}

	/**
	 * Updates customer id only if specified id value is not negative.
	 * 
	 * @param id specified id to update.
	 * @throws CouponSystemException if specified id value is negative.
	 */
	public void setId(int id) throws CouponSystemException {
		if (id < 0)
			throw new CouponSystemException("Failed to set customer id: value is negative!");
		this.id = id;
	}

	/**
	 * Updates last name of the current customer only if specified value not
	 * {@code null}, not empty and not blank.
	 * 
	 * @param lastName specified last name to update.
	 * @throws CouponSystemException if specified last name value is {@code null},
	 *                               empty or blank.
	 */
	public void setLastName(String lastName) throws CouponSystemException {
		if (lastName == null || lastName.isEmpty() || lastName.isBlank())
			throw new CouponSystemException("Failed to set customer last name: text is empty!");
		this.lastName = lastName;
	}

	/**
	 * Updates password of the current customer only if specified password value not
	 * {@code null}, not empty and not blank.
	 * 
	 * @param password specified password to update.
	 * @throws CouponSystemException if specified password value is {@code null},
	 *                               empty or blank.
	 */
	public void setPassword(String password) throws CouponSystemException {
		if (password == null || password.isEmpty() || password.isBlank())
			throw new CouponSystemException("Failed to set customer password: text is empty!");
		this.password = password;
	}

	@Override
	public String toString() {
		return "Customer [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", email=" + email
				+ ", password=" + password + "]";
	}

}
