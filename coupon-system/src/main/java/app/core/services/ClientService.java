package app.core.services;

import app.core.exceptions.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

/**
 * The class {@code ClientService} and it gives all subclasses option to login
 * into coupon system application.
 * 
 * @author Vitaly Zlobin
 *
 */
public abstract class ClientService {

	protected CompanyRepository companyRepository;
	protected CustomerRepository customerRepository;
	protected CouponRepository couponRepository;

	/**
	 * Performs connection to the DB by using specified parameters (email and
	 * password). Returns {@code true} if connection succeeded, otherwise -
	 * {@code false}.
	 * 
	 * @param email    specified email to connect with.
	 * @param password specified password to connect with.
	 * @return {@code true} if connection to the storage was succeeded, otherwise -
	 *         {@code false}.
	 * @throws CouponSystemException if failed to connect to storage with specified
	 *                               email and password.
	 */
	abstract boolean login(String email, String password) throws CouponSystemException;

}
