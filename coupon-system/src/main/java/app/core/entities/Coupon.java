package app.core.entities;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PreRemove;

import com.fasterxml.jackson.annotation.JsonIgnore;

import app.core.enums.Category;
import app.core.exceptions.CouponSystemException;

/**
 * The class {@code Coupon} is the bean with all available functionality for
 * coupon system application.
 * 
 * @author Vitaly Zlobin
 *
 */
@Entity
//@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Coupon {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@Column(name = "company_id")
	private int companyId;
	@Enumerated(EnumType.STRING)
	private Category category;
	private String title;
	private String description;
	@Column(name = "start_date")
	private LocalDate startDate;
	@Column(name = "end_date")
	private LocalDate endDate;
	private int amount;
	private double price;
	private String image;

	@JsonIgnore
	@ManyToMany(mappedBy = "coupons", cascade = { CascadeType.DETACH, CascadeType.REFRESH,
			CascadeType.PERSIST }, fetch = FetchType.LAZY)
	private List<Customer> customers;

	{
		this.customers = new ArrayList<>();
	}

	/**
	 * Initializes new coupon object with hard coded values (validated correct
	 * values).
	 * 
	 * @throws CouponSystemException if some validation was failed.
	 */
	public Coupon() throws CouponSystemException {
		this(0, 0, Category.CLOTHING, "DefaultCouponTitle", "DefaultCouponDescription", LocalDate.now(),
				LocalDate.now(), 0, 0.0, "DefaultCouponImage");
	}

	/**
	 * Initializes new coupon object only if specified values are passed all
	 * validations.
	 * 
	 * @param id          specified coupon id.
	 * @param companyId   specified company id of this coupon.
	 * @param category    specified coupon category.
	 * @param title       specified coupon title.
	 * @param description specified coupon description.
	 * @param startDate   specified coupon start date.
	 * @param endDate     specified coupon expiration date.
	 * @param amount      specified coupon quantity in stock.
	 * @param price       specified coupon price.
	 * @param image       specified coupon image.
	 * @throws CouponSystemException if some validation was failed.
	 */
	public Coupon(int id, int companyId, Category category, String title, String description, LocalDate startDate,
			LocalDate endDate, int amount, double price, String image) throws CouponSystemException {
		setId(id);
		setCompanyId(companyId);
		setCategory(category);
		setTitle(title);
		setDescription(description);
		setStartDate(startDate);
		setEndDate(endDate);
		setAmount(amount);
		setPrice(price);
		setImage(image);
	}

	@PreRemove
	public void deleteCouponsFromCustomers() {
		for (Customer customer : customers) {
			customer.getCoupons().remove(this);
		}
	}

	public void addCustomer(Customer customer) throws CouponSystemException {
		if (customer != null) {
			if (customers == null)
				customers = new ArrayList<>();
			// TODO: add coupon?
			customer.addCoupon(this);
			this.customers.add(customer);

			// TODO: ?????
//			if (customer.getCoupons() == null)
//				customer.setCoupons(new ArrayList<>());
//			customer.getCoupons().add(this);
		}
	}

	public void deleteCustomer(int customerId) throws CouponSystemException {
		if (id < 0)
			throw new CouponSystemException("Coupon failed to remove customer by id: value is negative!");
		Customer customer = new Customer();
		customer.setId(customerId);
		int index = customers.indexOf(customer);
		if (index == -1)
			throw new CouponSystemException("Coupon failed to remove customer by id: not found!");

		customer = customers.get(index);
		// remove association
		for (Customer c : customers) {
			c.getCoupons().remove(this);
		}
		customers.remove(customer);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Coupon other = (Coupon) obj;
		return id == other.id;
	}

	/**
	 * Returns integer that represents amount of these coupons available in stock.
	 * 
	 * @return returns amount of coupons in stock.
	 */
	public int getAmount() {
		return amount;
	}

	/**
	 * Returns {@link Category} that represents category of this coupon.
	 * 
	 * @return category of this coupon.
	 */
	public Category getCategory() {
		return category;
	}

	/**
	 * Returns integer that represents company id of this coupon.
	 * 
	 * @return company id of this coupon.
	 */
	public int getCompanyId() {
		return companyId;
	}

	/**
	 * Returns {@link List}<{@link Customer}> that represents list of customers that
	 * purchased this coupon.
	 * 
	 * @return list of this customer coupons purchases.
	 */
	public List<Customer> getCustomers() {
		return customers;
	}

	/**
	 * Returns {@link String} that represents description of this coupon.
	 * 
	 * @return description of this coupon.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns {@link LocalDate} that represents start date of this coupon.
	 * 
	 * @return start date of this coupon.
	 */
	public LocalDate getEndDate() {
		return endDate;
	}

	/**
	 * Returns integer that represents id of this coupon.
	 * 
	 * @return id of this coupon.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Returns {@link String} that represents image of this coupon.
	 * 
	 * @return image of this coupon.
	 */
	public String getImage() {
		return image;
	}

	/**
	 * Returns floating number that represents price of this coupon.
	 * 
	 * @return price of this coupon.
	 */
	public double getPrice() {
		return price;
	}

	/**
	 * Returns {@link LocalDate} that represents expiration date of this coupon.
	 * 
	 * @return expiration date of this coupon.
	 */
	public LocalDate getStartDate() {
		return startDate;
	}

	/**
	 * Returns {@link String} that represents title of this coupon.
	 * 
	 * @return title of this coupon.
	 */
	public String getTitle() {
		return title;
	}

	@Override
	public int hashCode() {
		return Objects.hash(id);
	}

	/**
	 * Updates amount of this coupon only if specified amount value is not negative.
	 * 
	 * @param amount specified coupons amount to update.
	 * @throws CouponSystemException if specified amount value is negative.
	 */
	public void setAmount(int amount) throws CouponSystemException {
		if (amount < 0)
			throw new CouponSystemException("Failed to set amount of coupons: value is negative!");
		this.amount = amount;
	}

	/**
	 * Updates category of this coupon only if specified category value not
	 * {@code null}.
	 * 
	 * @param category specified coupon category to update.
	 * @throws CouponSystemException if specified category is {@code null}.
	 */
	public void setCategory(Category category) throws CouponSystemException {
		if (category == null)
			throw new CouponSystemException("Failed to set category of the coupon: is empty!");
		this.category = category;
	}

	/**
	 * Updates company id of this coupon only if specified id value is not negative.
	 * 
	 * @param companyId specified company id of this coupon.
	 * @throws CouponSystemException if specified id value is negative.
	 */
	public void setCompanyId(int companyId) throws CouponSystemException {
		if (companyId < 0)
			throw new CouponSystemException("Failed to set company id of the coupon: value is negative!");
		this.companyId = companyId;
	}

	/**
	 * Sets list of customers that purchased this coupon only if it is not
	 * {@code null}.
	 * 
	 * @param customers specified list of customers that purchased this coupon.
	 */
	public void setCustomers(List<Customer> customers) {
		if (customers != null)
			this.customers = customers;
	}

	/**
	 * Updates description of this coupon only if specified description value is not
	 * {@code null}, not empty and not blank.
	 * 
	 * @param description specified coupon description.
	 * @throws CouponSystemException if specified description value is {@code null},
	 *                               empty or blank.
	 */
	public void setDescription(String description) throws CouponSystemException {
		if (description == null || description.isEmpty() || description.isBlank())
			throw new CouponSystemException("Failed to set coupon description: text is empty!");
		this.description = description;
	}

	/**
	 * Updates expiration date of this coupon only if specified end date value is
	 * not {@code null} and not before start date of this coupon.
	 * 
	 * @param endDate specified expiration date of this coupon.
	 * @throws CouponSystemException if specified expiration date value is
	 *                               {@code null} or before start date of this
	 *                               coupon.
	 */
	public void setEndDate(LocalDate endDate) throws CouponSystemException {
		if (endDate == null)
			throw new CouponSystemException("Failed to set coupon end date: is empty!");
		if (endDate.isBefore(startDate))
			throw new CouponSystemException("Failed to set coupon end date: end date is before start date!");
		this.endDate = endDate;
	}

	/**
	 * Updates id of this coupon only if specified id value not negative.
	 * 
	 * @param id specified coupon id .
	 * @throws CouponSystemException if specified id value is negative.
	 */
	public void setId(int id) throws CouponSystemException {
		if (id < 0)
			throw new CouponSystemException("Failed to set coupon id: value is negative!");
		this.id = id;
	}

	/**
	 * Updates image of this coupon only if specified image value not {@code null},
	 * not {@code empty} and not {@code blank}.
	 * 
	 * @param image specified coupon image to update.
	 * @throws CouponSystemException if specified image value is {@code null},
	 *                               {@code empty} or {@code blank}.
	 */
	public void setImage(String image) throws CouponSystemException {
		if (image == null || image.isEmpty() || image.isBlank())
			throw new CouponSystemException("Failed to set coupon image: text is empty!");
		this.image = image;
	}

	/**
	 * Updates price of this coupon only if specified price value is not negative.
	 * 
	 * @param price specified coupon price to update.
	 * @throws CouponSystemException if specified price value is negative.
	 */
	public void setPrice(double price) throws CouponSystemException {
		if (price < 0)
			throw new CouponSystemException("Failed to set coupon price: value is negative!");
		this.price = price;
	}

	/**
	 * Updates start date of this coupon only if specified start date value is not
	 * {@code null}.
	 * 
	 * @param startDate specified coupon start date to update.
	 * @throws CouponSystemException if specified start date value is {@code null}.
	 */
	public void setStartDate(LocalDate startDate) throws CouponSystemException {
		if (startDate == null)
			throw new CouponSystemException("Failed to set coupon start date: is empty!");
		this.startDate = startDate;
	}

	/**
	 * Updates title of this coupon only if specified title value is not
	 * {@code null}, not {@code empty} and not {@code blank}.
	 * 
	 * @param title specified coupon title.
	 * @throws CouponSystemException if specified title value is {@code null},
	 *                               {@code empty} or {@code blank}.
	 */
	public void setTitle(String title) throws CouponSystemException {
		if (title == null || title.isEmpty() || title.isBlank())
			throw new CouponSystemException("Failed to set coupon title: text is empty!");
		this.title = title;
	}

	@Override
	public String toString() {
		return "Coupon [\n\tid=" + id + ", \n\tcompanyId=" + companyId + ", \n\tcategory=" + category + ", \n\ttitle="
				+ title + ", \n\tdescription=" + description + ", \n\tstartDate=" + startDate + ", \n\tendDate="
				+ endDate + ", \n\tamount=" + amount + ", \n\tprice=" + price + ", \n\timage=" + image + "\n]";
	}

}
