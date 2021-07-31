package app.core.entities;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

import app.core.exceptions.CouponSystemException;

/**
 * The class {@code Company} is the bean with all available functionality for
 * coupon system application.
 * 
 * @author Vitaly Zlobin
 *
 */
@Entity
public class Company {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	private String name;
	private String email;
	private String password;
	@OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	@JoinColumn(name = "company_id")
	private List<Coupon> coupons;

	{
		this.coupons = new ArrayList<>();
	}

	/**
	 * Initializes new company object with hard coded values (validated correct
	 * values).
	 * 
	 * @throws CouponSystemException if some validation was failed.
	 */
	public Company() throws CouponSystemException {
		this(0, "DefaultCompanyName", "DefaultCompanyEmail", "DefaultCompanyPassword");
	}

	/**
	 * Initializes new company object only if specified values are passed all
	 * validations.
	 * 
	 * @param id       specified company id.
	 * @param name     specified company name.
	 * @param email    specified company email.
	 * @param password specified company password.
	 * @throws CouponSystemException if some validation was failed.
	 */
	public Company(int id, String name, String email, String password) throws CouponSystemException {
		setId(id);
		setName(name);
		setEmail(email);
		setPassword(password);
	}

	/**
	 * Adds specified coupon to this company coupons list only if specified value
	 * not {@code null}.
	 * 
	 * @param coupon specified coupon to add.
	 * @throws CouponSystemException if specified coupon is {@code null}.
	 */
	public void addCoupon(Coupon coupon) throws CouponSystemException {
		if (coupon == null)
			throw new CouponSystemException("Company failed to add new coupon: is empty!");

		if (coupons != null) {
			int index = coupons.indexOf(coupon);
			if (index != -1)
				throw new CouponSystemException("Company failed to add new coupon: already axists!");
		} else
			coupons = new ArrayList<>();

		coupon.setCompanyId(this.id);
		coupons.add(coupon);
	}

	/**
	 * Removes coupon from this company coupons list by specified coupon id, if
	 * exists.
	 * 
	 * @param id specified coupon id.
	 * @throws CouponSystemException if specified coupon id is negative.
	 */
	public void deleteCoupon(int id) throws CouponSystemException {
		if (id < 0)
			throw new CouponSystemException("Company failed to remove coupon by id: value is negative!");
		Coupon coupon = new Coupon();
		coupon.setId(id);
		if (!coupons.remove(coupon))
			throw new CouponSystemException("Company failed to remove coupon by id: not found!");

//		int indexOf = coupons.indexOf(coupon);
//		if (indexOf == -1)
//			throw new CouponSystemException("Company failed to remove coupon by id: not found!");
//		coupons.remove(indexOf);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Company other = (Company) obj;
		return id == other.id;
	}

	/**
	 * Returns {@link List}<{@link Coupon}> that represents list of this company
	 * coupons.
	 * 
	 * @return list of this company coupons.
	 */
	public List<Coupon> getCoupons() {
		return coupons;
	}

	/**
	 * Returns {@link String} that represents email of this company.
	 * 
	 * @return email of this company.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Returns integer that represents id of this company.
	 * 
	 * @return id of this company.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns {@link String} that represents name of this company.
	 * 
	 * @return name of this company.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Returns {@link String} that represents password of this company.
	 * 
	 * @return password of this company.
	 */
	public String getPassword() {
		return password;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Prints all available coupons of this company to console.
	 */
	public void printAllCoupons() {
		if (coupons.isEmpty())
			System.out.println(this.name + " don't have coupons to display");
		else {
			int count = 1;
			System.out.println("List of " + this.name + "'s coupons:");
			for (Coupon coupon : coupons) {
				System.out.println("   " + count++ + ") " + coupon);
			}
		}
	}

	/**
	 * Sets company coupons list to specified only if it is not {@code null}.
	 * 
	 * @param coupons specified list to set.
	 */
	public void setCoupons(List<Coupon> coupons) {
		if (coupons != null)
			this.coupons = coupons;
	}

	/**
	 * Updates email of the current company only if specified email value not
	 * {@code null}, not empty and not blank.
	 * 
	 * @param email specified email to update.
	 * @throws CouponSystemException if specified email value is {@code null}, empty
	 *                               or blank.
	 */
	public void setEmail(String email) throws CouponSystemException {
		if (email == null || email.isEmpty() || email.isBlank())
			throw new CouponSystemException("Failed to set company email: text is empty!");
		this.email = email;
	}

	/**
	 * Updates company id only if specified id value is not negative.
	 * 
	 * @param id specified id to update.
	 * @throws CouponSystemException if specified id value is negative.
	 */
	public void setId(int id) throws CouponSystemException {
		if (id < 0)
			throw new CouponSystemException("Failed to set company id: value is negative!");
		this.id = id;
	}

	/**
	 * Updates current company name only if specified value not {@code null}, not
	 * empty and not blank.
	 * 
	 * @param name specified company name to update.
	 * @throws CouponSystemException if specified name is {@code null}, empty or
	 *                               blank.
	 */
	public void setName(String name) throws CouponSystemException {
		if (name == null || name.isEmpty() || name.isBlank())
			throw new CouponSystemException("Failed to set company name: text is empty!");
		this.name = name;
	}

	/**
	 * Update current company password only if specified value is not {@code null},
	 * not empty and not blank.
	 * 
	 * @param password specified password to update.
	 * @throws CouponSystemException if specified value {@code null}, empty or
	 *                               blank.
	 */
	public void setPassword(String password) throws CouponSystemException {
		if (password == null || password.isEmpty() || password.isBlank())
			throw new CouponSystemException("Failed to set company password: text is empty!");
		this.password = password;
	}

	@Override
	public String toString() {
		return "Company [id=" + id + ", name=" + name + ", email=" + email + ", password=" + password + "]";
	}

}
