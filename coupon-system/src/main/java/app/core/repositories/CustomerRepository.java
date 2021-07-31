package app.core.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Customer;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {

	/**
	 * Retrieves entity by its email and password.
	 * 
	 * @param email    specified email to check.
	 * @param password specified password to check.
	 * @return entity with specified email and password.
	 */
	Customer findByEmailIgnoreCaseAndPassword(String email, String password);

	/**
	 * Retrieves entity with specified email if found.
	 * 
	 * @param email specified email to check.
	 * @return entity if found.
	 */
	Customer findByEmailIgnoreCase(String email);

	// if coupons was purchased
//	boolean existsByIdAndCouponsId(int customerId, int couponId);

}
