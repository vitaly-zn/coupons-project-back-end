package app.core.services;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.enums.Category;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

/**
 * The class {@code CustomerService} represents business logic layer for coupon
 * system application.
 * 
 * @author VItaly Zlobin
 *
 */
@Service
@Transactional
@Scope("prototype")
public class CustomerService extends ClientService {

//	private int customerId; // id of customer that trying to login

	public CustomerService(CustomerRepository customerRepository, CouponRepository couponRepository) {
		this.customerRepository = customerRepository;
		this.couponRepository = couponRepository;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		try {
			Customer loggedInCustomer = getCustomerByEmailAndPassword(email, password);
			if (loggedInCustomer == null)
				return false;

//			this.customerId = loggedInCustomer.getId();
			return true;
		} catch (Exception e) {
			throw new CouponSystemException(
					"customer login failed: " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}
	}

	/**
	 * Checks if currently logged in customer have permission to purchase specified
	 * coupon and returns updated coupon stock after purchase.<br>
	 * Allowed only if: (1) not purchased in past (2) available in stock (3) not
	 * expired.<br>
	 * If allowed - adds purchase and updates storage for changes.
	 * 
	 * @param coupon specified coupon to purchase.
	 * @return purchased entity with updated stock.
	 * @throws CouponSystemException if failed to purchase specified coupon.
	 */
	public Coupon purchaseCoupon(int id, Coupon coupon) throws CouponSystemException {
		if (coupon == null)
			throw new CouponSystemException("failed to purchase coupon: is null!");

		// 1) if coupon exists
		Coupon couponFromDB;
		Optional<Coupon> opt = couponRepository.findById(coupon.getId());
		if (opt.isPresent())
			couponFromDB = opt.get();
		else
			throw new CouponSystemException(
					"failed to purchase coupon: coupon with id = " + coupon.getId() + " not found!");

		// 2) if not purchased
//		if (customerRepository.existsByIdAndCouponsId(this.customerId, coupon.getId()))
		Customer customer = getCustomerDetailsWithCoupons(id);
		if (customer.getCoupons().contains(coupon))
			throw new CouponSystemException("failed to purchase coupon: coupon with id = " + coupon.getId()
					+ " already purchased in the past (only 1 purchase per coupon allowed)!");

		// 3) if available in stock
		if (couponFromDB.getAmount() == 0)
			throw new CouponSystemException(
					"failed to purchase coupon: coupon with id = " + coupon.getId() + " out of stock!");

		// 4) if not expired
		if (couponFromDB.getEndDate().isBefore(LocalDate.now()))
			throw new CouponSystemException("failed to purchase coupon: coupon with id = " + coupon.getId()
					+ " expired at " + coupon.getEndDate() + "!");

		// TODO:

		// 5) update coupon amount changes
		couponFromDB.setAmount(couponFromDB.getAmount() - 1);

		// 6) purchase
//		Customer customer = customerRepository.getOne(this.customerId);
		customer.addCoupon(couponFromDB);

		return couponFromDB;
	}

	/**
	 * Retrieves entity by its id.
	 * 
	 * @param couponId entity id.
	 * @return return entity if found.
	 * @throws CouponSystemException if failed to retrieve entity.
	 */
	public Coupon getOneCoupon(int couponId) throws CouponSystemException {
		Optional<Coupon> opt = couponRepository.findById(couponId);
		if (opt.isPresent())
			return opt.get();

		throw new CouponSystemException("coupon with id = " + couponId + " not found!");
	}

	/**
	 * Returns all available coupons.
	 * 
	 * @return all available coupons.
	 * @throws CouponSystemException if failed to get list of coupons.
	 */
	public List<Coupon> getAllAvailableCoupons() throws CouponSystemException {
		List<Coupon> coupons;
		try {
//			coupons = couponRepository.findAllByCustomersIdNot(this.customerId);
			coupons = couponRepository.findAll();
		} catch (Exception e) {
			throw new CouponSystemException(
					"failed to retrieve all available coupons to purchsed for current customer: " + e.getMessage()
							+ (e.getCause() != null ? ": " + e.getCause() : ""),
					e);
		}
		return coupons;
	}

	/**
	 * Returns list of all purchased coupons by currently logged in customer.
	 * 
	 * @return list of purchased coupons.
	 * @throws CouponSystemException if failed to get list of purchased coupons.
	 */
	public List<Coupon> getAllCustomerCoupons(int id) throws CouponSystemException {
		List<Coupon> coupons;
		try {
			coupons = couponRepository.findAllByCustomersId(id);
		} catch (Exception e) {
			throw new CouponSystemException("failed to retrieve all purchsed by customer coupons: " + e.getMessage()
					+ (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return coupons;
	}

	/**
	 * Returns list of all purchased coupons, with specified category, by currently
	 * logged in customer.
	 * 
	 * @param category specified coupon category.
	 * @return list of purchased coupons.
	 * @throws CouponSystemException if failed to get list of purchased coupons.
	 */
	public List<Coupon> getAllCustomerCoupons(int id, Category category) throws CouponSystemException {
		List<Coupon> coupons;
		try {
			coupons = couponRepository.findAllByCustomersIdAndCategory(id, category);
		} catch (Exception e) {
			throw new CouponSystemException(
					"failed to retrieve all purshased by customer coupons with specified category(" + category + "): "
							+ e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""),
					e);
		}

		return coupons;
	}

	/**
	 * Returns list of all purchased coupons with price, that less then specified
	 * value, by currently logged in customer.
	 * 
	 * @param maxPrice specified max value.
	 * @return list of purchased coupons.
	 * @throws CouponSystemException if failed to get list of purchased coupons.
	 */
	public List<Coupon> getAllCustomerCoupons(int id, double maxPrice) throws CouponSystemException {
		List<Coupon> coupons;
		try {
			coupons = couponRepository.findAllByCustomersIdAndPriceLessThan(id, maxPrice);
		} catch (Exception e) {
			throw new CouponSystemException(
					"failed to retrieve all purchased by customer coupons with specified max price(" + maxPrice + "): "
							+ e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""),
					e);
		}

		return coupons;
	}

	/**
	 * Returns the {@link Customer} that contains all details of currently logged in
	 * customer (WITHOUT ITS COUPONS) if found.
	 * 
	 * @return entity if found.
	 * @throws CouponSystemException if failed to get customer entity.
	 */
	public Customer getCustomerDetails(int id) throws CouponSystemException {
		Optional<Customer> opt = customerRepository.findById(id);
		if (opt.isPresent())
			return opt.get();

		throw new CouponSystemException("failed to get customer details: customer with id = " + id + " not found!");
	}

	/**
	 * Returns the {@link Customer} that contains all details of currently logged in
	 * customer (WITH ALL ITS COUPONS) if found.
	 * 
	 * @return entity if found.
	 * @throws CouponSystemException if failed to get customer entity.
	 */
	public Customer getCustomerDetailsWithCoupons(int id) throws CouponSystemException {
		Customer customer = getCustomerDetails(id);
		customer.setCoupons(getAllCustomerCoupons(id));
		return customer;
	}

	public Customer getCustomerByEmailAndPassword(String email, String password) {
		return customerRepository.findByEmailIgnoreCaseAndPassword(email, password);
	}

}
