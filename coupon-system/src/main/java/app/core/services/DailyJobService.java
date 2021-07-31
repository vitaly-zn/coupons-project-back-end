package app.core.services;

import java.time.LocalDate;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;

import app.core.exceptions.CouponSystemException;
import app.core.repositories.CouponRepository;

/**
 * The class {@code DailyJobService} represents business logic layer for coupon
 * system application.
 * 
 * @author VItaly Zlobin
 *
 */
@Service
@Transactional
public class DailyJobService {

	private CouponRepository couponRepository;

	public DailyJobService(CouponRepository couponRepository) {
		this.couponRepository = couponRepository;
	}

	public long deleteExpiredCoupons(LocalDate endDate) throws CouponSystemException {

		// TODO:
		// delete all coupon purchases
		// "delete from `CUSTOMERS_VS_COUPONS` where `COUPON_ID` = any (select `ID` from
		// `COUPONS` where `END_DATE` < ?)";

		// delete expired coupons
		// "delete from `COUPONS` where `END_DATE` < ?"

		return couponRepository.deleteAllByEndDateBefore(LocalDate.now());
	}

}
