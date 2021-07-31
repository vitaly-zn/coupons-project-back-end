package app.core.repositories;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Coupon;
import app.core.enums.Category;

public interface CouponRepository extends JpaRepository<Coupon, Integer> {

	/**
	 * Returns entity with specified company id and coupon title, if found.
	 * 
	 * @param companyId specified company id to check.
	 * @param title     specified coupon title to check.
	 * @return entity if found.
	 */
	Coupon findByCompanyIdAndTitle(int companyId, String title);

	/**
	 * Returns list of all available coupons of specified by id company.
	 * 
	 * @param companyId specified company id.
	 * @return list of all available company coupons.
	 */
	List<Coupon> findAllByCompanyId(int companyId);

	/**
	 * Returns list of all available coupons with specified category of specified by
	 * id company.
	 * 
	 * @param companyId specified company id.
	 * @param category  specified coupon category.
	 * @return list of all available company coupons with specified coupon category.
	 */
	List<Coupon> findAllByCompanyIdAndCategory(int companyId, Category category);

	/**
	 * Returns list of available coupons with price, that less than specified value,
	 * of specified by id company.
	 * 
	 * @param companyId specified company id.
	 * @param price     specified max price.
	 * @return list of all available company coupons with specified max coupon
	 *         price.
	 */
	List<Coupon> findAllByCompanyIdAndPriceLessThan(int companyId, double price);

	List<Coupon> findAllByCustomersId(int customerId);

	List<Coupon> findAllByCustomersIdAndCategory(int customerId, Category category);

	List<Coupon> findAllByCustomersIdAndPriceLessThan(int customerId, double price);

	// removes expired coupons
	long deleteAllByEndDateBefore(LocalDate endDate);

	// List of coupons not purchased by specified by id customer.
//	List<Coupon> findAllByCustomersIdNot(int customerId);

}
