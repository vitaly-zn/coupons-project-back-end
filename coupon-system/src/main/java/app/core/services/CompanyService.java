package app.core.services;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.enums.Category;
import app.core.exceptions.CouponSystemException;
import app.core.repositories.CompanyRepository;
import app.core.repositories.CouponRepository;

/**
 * The class {@code CompanyService} represents business logic layer for coupon
 * system application.
 * 
 * @author VItaly Zlobin
 *
 */
@Service
@Transactional
@Scope("prototype")
public class CompanyService extends ClientService {

//	private int companyId; // id of company that trying to login

	public CompanyService(CompanyRepository companyRepository, CouponRepository couponRepository) {
		this.companyRepository = companyRepository;
		this.couponRepository = couponRepository;
	}

	@Override
	public boolean login(String email, String password) throws CouponSystemException {
		try {
			Company loggedInCompany = getCompanyByEmailAndPassword(email, password);
			if (loggedInCompany == null)
				return false;

//			this.companyId = loggedInCompany.getId();
			return true;
		} catch (Exception e) {
			throw new CouponSystemException(
					"company login failed: " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}
	}

	/**
	 * Saves a specified coupon (for currently logged in company) only if current
	 * company do not have coupon with same title and specified coupon not
	 * {@code null}. Returns saved coupon if succeeded.
	 * 
	 * @param coupon specified coupon to save (must be not {@code null}).
	 * @return saved coupon.
	 * @throws CouponSystemException if failed to save specified coupon.
	 */
	public Coupon addCoupon(int id, Coupon coupon) throws CouponSystemException {
		if (coupon == null)
			throw new CouponSystemException("failed to save coupon: is null!");

		if (couponRepository.findByCompanyIdAndTitle(id, coupon.getTitle()) != null)
			throw new CouponSystemException("failed to save coupon: title(" + coupon.getTitle() + ") already in use!");

		Coupon savedCoupon;
		try {
			coupon.setCompanyId(id);
			savedCoupon = couponRepository.save(coupon);
		} catch (Exception e) {
			throw new CouponSystemException("failed to save coupon to company: " + e.getMessage()
					+ (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return savedCoupon;
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
	 * Returns deleted by id coupon and all its purchases history if found.
	 * 
	 * @param couponId specified coupon id.
	 * @return deleted entity.
	 * @throws CouponSystemException if failed to delete coupon.
	 */
	public Coupon deleteCoupon(int couponId) throws CouponSystemException {
		// TODO: cascading to remove purchases...
		Coupon coupon;
		try {
			coupon = getOneCoupon(couponId);
		} catch (CouponSystemException e) {
			throw new CouponSystemException("failed to delete coupon: " + e.getMessage(), e);
		}

		try {
			couponRepository.delete(coupon);
		} catch (Exception e) {
			throw new CouponSystemException(
					"failed to delete coupon: " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""),
					e);
		}

		return coupon;
	}

	/**
	 * Returns list of all available coupons of currently logged in company.
	 * 
	 * @return list of all company coupons.
	 * @throws CouponSystemException if failed to get list of company coupons.
	 */
	public List<Coupon> getAllCompanyCoupons(int id) throws CouponSystemException {
		List<Coupon> coupons;
		try {
			coupons = couponRepository.findAllByCompanyId(id);
		} catch (Exception e) {
			throw new CouponSystemException("failed to retrieve all available company coupons: " + e.getMessage()
					+ (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}

		return coupons;
	}

	/**
	 * Returns list of all available coupons with specified category for currently
	 * logged in company.
	 * 
	 * @param category specified coupon category.
	 * @return list of all company coupons with specified coupon category.
	 * @throws CouponSystemException if failed to get list of company coupons.
	 */
	public List<Coupon> getAllCompanyCoupons(int id, Category category) throws CouponSystemException {
		List<Coupon> coupons;
		try {
			coupons = couponRepository.findAllByCompanyIdAndCategory(id, category);
		} catch (Exception e) {
			throw new CouponSystemException("failed to retrieve all available company coupons by specified category("
					+ category + "): " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}
		return coupons;
	}

	/**
	 * Returns list of all available coupons with price, that less than specified
	 * value, for currently logged in company.
	 * 
	 * @param maxPrice specified max price.
	 * @return list of all company coupons with specified max coupon price.
	 * @throws CouponSystemException if failed to get list of company coupons.
	 */
	public List<Coupon> getAllCompanyCoupons(int id, double maxPrice) throws CouponSystemException {
		List<Coupon> coupons;
		try {
			coupons = couponRepository.findAllByCompanyIdAndPriceLessThan(id, maxPrice);
		} catch (Exception e) {
			throw new CouponSystemException("failed to retrieve all available company coupons by specified max price("
					+ maxPrice + "): " + e.getMessage() + (e.getCause() != null ? ": " + e.getCause() : ""), e);
		}
		return coupons;
	}

	/**
	 * Returns updated entity (details will be updated: category, title,
	 * description, start date, end date, amount, price and image) only if specified
	 * entity {@code != null} and entity with same title not exists.
	 * 
	 * @param coupon specified entity to update (must be not {@code null}).
	 * @return updated entity.
	 * @throws CouponSystemException if failed to update specified entity.
	 */
	public Coupon updateCoupon(int id, Coupon coupon) throws CouponSystemException {
		if (coupon == null)
			throw new CouponSystemException("failed to update coupon: is null!");

		Coupon couponFromDb;
		try {
			couponFromDb = getOneCoupon(coupon.getId());
		} catch (CouponSystemException e) {
			throw new CouponSystemException("failed to update coupon: " + e.getMessage(), e);
		}

		// if coupon belongs to other company...(?)
		if (couponFromDb.getCompanyId() != id)
			throw new CouponSystemException("failed to update coupon: not found!");

		Coupon foundCoupon = couponRepository.findByCompanyIdAndTitle(id, coupon.getTitle());
		if (foundCoupon != null && !foundCoupon.equals(couponFromDb))
			throw new CouponSystemException(
					"failed to update coupon: title(" + coupon.getTitle() + ") already in use!");

		try {
			couponFromDb.setCategory(coupon.getCategory());
			couponFromDb.setTitle(coupon.getTitle());
			couponFromDb.setDescription(coupon.getDescription());
			couponFromDb.setStartDate(coupon.getStartDate());
			couponFromDb.setEndDate(coupon.getEndDate());
			couponFromDb.setAmount(coupon.getAmount());
			couponFromDb.setPrice(coupon.getPrice());
			couponFromDb.setImage(coupon.getImage());
		} catch (CouponSystemException e) {
			throw new CouponSystemException("failed to update company: " + e.getMessage(), e);
		}

		return couponFromDb;
	}

	/**
	 * Returns the {@link Company} entity that contains all details of currently
	 * logged in company (WITHOUT ITS COUPONS) if found.
	 * 
	 * @return entity if found.
	 * @throws CouponSystemException if failed to get company entity.
	 */
	public Company getCompanyDetails(int id) throws CouponSystemException {
		Optional<Company> opt = companyRepository.findById(id);
		if (opt.isPresent())
			return opt.get();

		throw new CouponSystemException("failed to get company details: company with id = " + id + " not found!");
	}

	/**
	 * Returns the {@link Company} entity that contains all details of currently
	 * logged in company (WITH ALL ITS COUPONS) if found.
	 * 
	 * @return entity if found.
	 * @throws CouponSystemException if failed to get company entity.
	 */
	public Company getCompanyDetailsWithCoupons(int id) throws CouponSystemException {
		Company company = getCompanyDetails(id);
		company.setCoupons(getAllCompanyCoupons(id));
		return company;
	}

	public Company getCompanyByEmailAndPassword(String email, String password) {
		return companyRepository.findByEmailIgnoreCaseAndPassword(email, password);
	}
}
