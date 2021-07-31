package app.core.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;
import app.core.repositories.CustomerRepository;

/**
 * The class {@code AdminService} represents business logic layer for coupon
 * system application.
 * 
 * @author VItaly Zlobin
 *
 */
@Service
@Transactional
@Scope("prototype")
public class AdminService extends ClientService {

	private String email;
	private String password;

	public AdminService(CompanyRepository companyRepository, CustomerRepository customerRepository,
			CouponRepository couponRepository, @Value("${admin.email}") String email,
			@Value("${admin.password}") String password) {
		this.companyRepository = companyRepository;
		this.customerRepository = customerRepository;
		this.couponRepository = couponRepository;
		this.email = email;
		this.password = password;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		try {
			return this.email.equals(email) && this.password.equals(password);
		} catch (Exception e) {
			throw new CouponSystemException(
					"admin login failed: " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}
	}

	/**
	 * Returns saved entity, only if specified entity not {@code null} and entity
	 * with same name or email not exists.
	 * 
	 * @param company specified entity to save (must not be null).
	 * @return saved entity.
	 * @throws CouponSystemException if specified entity is {@code null} or entity
	 *                               with same name or email already exists.
	 */
	public Company addCompany(Company company) throws CouponSystemException {
		if (company == null)
			throw new CouponSystemException("failed to save company: is null!");

		List<Company> foundCompanies = companyRepository.findByNameOrEmailIgnoreCase(company.getName(),
				company.getEmail());
		if (!foundCompanies.isEmpty()) {
			for (Company comp : foundCompanies) {
				if (comp != null) {
					if (comp.getName().equals(company.getName()))
						throw new CouponSystemException(
								"failed to save company: name(" + company.getName() + ") already in use!");
					if (comp.getEmail().equalsIgnoreCase(company.getEmail()))
						throw new CouponSystemException(
								"failed to save company: email(" + company.getEmail() + ") already in use!");
				}
			}
		}

		Company savedCompany;
		try {
			savedCompany = companyRepository.save(company);
		} catch (Exception e) {
			throw new CouponSystemException(
					"failed to save company: " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return savedCompany;
	}

	/**
	 * Retrieves company by specified id.
	 * 
	 * @param companyId specified id.
	 * @return company with specified id if found.
	 * @throws CouponSystemException if company with specified id not exists.
	 */
	public Company getOneCompany(int companyId) throws CouponSystemException {
		Optional<Company> opt = companyRepository.findById(companyId);
		if (opt.isPresent())
			return opt.get();
		throw new CouponSystemException("company with id = " + companyId + " not found!");
	}

	/**
	 * Retrieves company by specified id and all its coupons.
	 * 
	 * @param companyId specified id.
	 * @return company with specified id and its coupons if found.
	 * @throws CouponSystemException if company with specified id not exists.
	 */
	public Company getOneCompanyWithCoupons(int companyId) throws CouponSystemException {
		Company company = getOneCompany(companyId);
		try {
			company.setCoupons(couponRepository.findAllByCompanyId(companyId));
		} catch (Exception e) {
			throw new CouponSystemException("failed to get company with id = " + companyId + ": " + e.getMessage()
					+ (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return company;
	}

	/**
	 * Returns all available companies.
	 * 
	 * @return list of all available companies.
	 * @throws CouponSystemException if failed to retrieve list of companies.
	 */
	public List<Company> getAllCompanies() throws CouponSystemException {
		List<Company> companies;
		try {
			companies = companyRepository.findAll();
		} catch (Exception e) {
			throw new CouponSystemException("failed to retrieve all available companies: " + e.getMessage()
					+ (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return companies;
	}

	/**
	 * Returns all available companies with its coupons.
	 * 
	 * @return list of all available companies and its coupons.
	 * @throws CouponSystemException if failed to retrieve list of companies.
	 */
	public List<Company> getAllCompaniesWithCoupons() throws CouponSystemException {
		List<Company> companies;
		try {
			companies = companyRepository.findAll();
			for (Company company : companies) {
				company.setCoupons(couponRepository.findAllByCompanyId(company.getId()));
			}
		} catch (Exception e) {
			throw new CouponSystemException("failed to retrieve all available companies: " + e.getMessage()
					+ (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return companies;
	}

	/**
	 * Returns updated entity (details will be updated: email and password) only if
	 * specified entity {@code != null} and entity with same email or name not
	 * exists.
	 * 
	 * @param company specified entity to update (must be not {@code null}).
	 * @return updated entity.
	 * @throws CouponSystemException if failed update specified entity.
	 */
	public Company updateComapany(Company company) throws CouponSystemException {
		if (company == null)
			throw new CouponSystemException("failed to update company: is null!");

		Company companyFromDB;
		try {
			companyFromDB = getOneCompany(company.getId());
		} catch (CouponSystemException e) {
			throw new CouponSystemException("failed to update company: " + e.getMessage(), e);
		}

		List<Company> foundCompanies = companyRepository.findByNameOrEmailIgnoreCase(company.getName(),
				company.getEmail());
		if (!foundCompanies.isEmpty()) {
			for (Company comp : foundCompanies) {
				if (comp != null) {
					if (comp.getName().equals(company.getName()) && !comp.equals(company))
						throw new CouponSystemException(
								"failed to update company: name(" + company.getName() + ") already in use!");
					if (comp.getEmail().equalsIgnoreCase(company.getEmail()) && !comp.equals(company))
						throw new CouponSystemException(
								"failed to update company: email(" + company.getEmail() + ") already in use!");
				}
			}
		}

		try {
			companyFromDB.setEmail(company.getEmail());
			companyFromDB.setPassword(company.getPassword());
		} catch (CouponSystemException e) {
			throw new CouponSystemException("failed to update company: " + e.getMessage(), e);
		}

		return companyFromDB;
	}

	/**
	 * Returns deleted entity with specified id, all its coupons and all its coupons
	 * purchase history, if found.
	 * 
	 * @param companyId specified entity id.
	 * @return deleted entity, if found.
	 * @throws CouponSystemException if failed to delete entity with all coupons and
	 *                               sales.
	 */
	public Company deleteCompany(int companyId) throws CouponSystemException {
		Company company;
		try {
			company = getOneCompany(companyId);
		} catch (CouponSystemException e) {
			throw new CouponSystemException("failed to delete company: " + e.getMessage(), e);
		}

		try {
			companyRepository.delete(company);
		} catch (Exception e) {
			throw new CouponSystemException(
					"failed to delete company: " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""),
					e);
		}

		return company;
	}

	/**
	 * Returns saved entity, only if specified entity is not {@code null} and entity
	 * with same email not exists.
	 * 
	 * @param customer specified entity to save (must be not {@code null}).
	 * @return saved entity.
	 * @throws CouponSystemException if specified entity is {@code null} or entity
	 *                               with same email already exists.
	 */
	public Customer addCustomer(Customer customer) throws CouponSystemException {
		if (customer == null)
			throw new CouponSystemException("failed to save customer: is null!");

		if (customerRepository.findByEmailIgnoreCase(customer.getEmail()) != null)
			throw new CouponSystemException(
					"failed to save customer: email(" + customer.getEmail() + ") already in use!");

		Customer savedCustomer;
		try {
			savedCustomer = customerRepository.save(customer);
		} catch (Exception e) {
			throw new CouponSystemException(
					"failed to save customer: " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""),
					e);
		}

		return savedCustomer;
	}

	/**
	 * Retrieves customer by specified id.
	 * 
	 * @param customerId specified customer id.
	 * @return customer with specified id.
	 * @throws CouponSystemException if customer with specified id not exists.
	 */
	public Customer getOneCustomer(int customerId) throws CouponSystemException {
		Optional<Customer> opt = customerRepository.findById(customerId);
		if (opt.isPresent())
			return opt.get();
		throw new CouponSystemException("customer with id = " + customerId + " not found!");
	}

	/**
	 * Retrieves customer and its purchased coupons by specified id.
	 * 
	 * @param customerId specified customer id.
	 * @return customer with all coupons purchases by specified id.
	 * @throws CouponSystemException if customer with specified id not exists.
	 */
	public Customer getOneCustomerWithCoupons(int customerId) throws CouponSystemException {
		Customer customer = getOneCustomer(customerId);
		try {
			customer.setCoupons(couponRepository.findAllByCustomersId(customerId));
		} catch (Exception e) {
			throw new CouponSystemException("failed to get customer with id = " + customerId + ": " + e.getMessage()
					+ (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return customer;
	}

	/**
	 * Returns all available customers.
	 * 
	 * @return list of all available customers.
	 * @throws CouponSystemException if failed to retrieve list of customers.
	 */
	public List<Customer> getAllCustomers() throws CouponSystemException {
		List<Customer> customers;
		try {
			customers = customerRepository.findAll();
		} catch (Exception e) {
			throw new CouponSystemException("failed to retrieve all available customers: " + e.getMessage()
					+ (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return customers;
	}

	/**
	 * Returns all available customers with coupons purchases.
	 * 
	 * @return list of all available customers with coupons purchases.
	 * @throws CouponSystemException if failed to retrieve list of customers.
	 */
	public List<Customer> getAllCustomersWithCoupons() throws CouponSystemException {
		List<Customer> customers;
		try {
			customers = customerRepository.findAll();
			for (Customer customer : customers) {
				customer.setCoupons(couponRepository.findAllByCustomersId(customer.getId()));
			}
		} catch (Exception e) {
			throw new CouponSystemException("failed to retrieve all available customers: " + e.getMessage()
					+ (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return customers;
	}

	/**
	 * Returns updated entity (details will be updated: first name, last name, email
	 * and password) only if specified entity {@code != null} and entity with same
	 * email not exists.
	 * 
	 * @param customer specified entity to update (must be not {@code null}).
	 * @return updated entity.
	 * @throws CouponSystemException if failed to update specified customer.
	 */
	public Customer updateCustomer(Customer customer) throws CouponSystemException {
		if (customer == null)
			throw new CouponSystemException("failed to update customer: is null!");

		Customer customerFromDB;
		try {
			customerFromDB = getOneCustomer(customer.getId());
		} catch (CouponSystemException e) {
			throw new CouponSystemException("failed to update customer: " + e.getMessage(), e);
		}

		Customer foundCustomer = customerRepository.findByEmailIgnoreCase(customer.getEmail());
		if (foundCustomer != null && !foundCustomer.equals(customer))
			throw new CouponSystemException(
					"failed to update customer: email(" + customer.getEmail() + ") already in use!");

		try {
			customerFromDB.setFirstName(customer.getFirstName());
			customerFromDB.setLastName(customer.getLastName());
			customerFromDB.setEmail(customer.getEmail());
			customerFromDB.setPassword(customer.getPassword());
		} catch (Exception e) {
			throw new CouponSystemException("failed to update customer: " + e.getMessage(), e);
		}

		return customerFromDB;
	}

	/**
	 * Returns deleted entity with specified id and all its coupons purchase
	 * history, if found.
	 * 
	 * @param customerId specified entity id.
	 * @return deleted entity, if found.
	 * @throws CouponSystemException if failed to delete entity with all coupons
	 *                               purchases.
	 */
	public Customer deleteCustomer(int customerId) throws CouponSystemException {
		Customer customer;
		try {
			customer = getOneCustomer(customerId);
		} catch (CouponSystemException e) {
			throw new CouponSystemException("failed to delete customer: " + e.getMessage(), e);
		}

		try {
			customerRepository.delete(customer);
		} catch (Exception e) {
			throw new CouponSystemException(
					"failed to delete customer: " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""),
					e);
		}

		return customer;
	}

}
