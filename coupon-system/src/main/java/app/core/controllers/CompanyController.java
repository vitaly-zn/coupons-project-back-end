package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.enums.Category;
import app.core.services.CompanyService;

@RestController
@RequestMapping("/api/company")
public class CompanyController {

	private CompanyService companyService;

	@Autowired
	public CompanyController(CompanyService companyService) {
		this.companyService = companyService;
	}

	@PostMapping("/{id}/add/coupon")
	public ResponseEntity<Coupon> addCoupon(@RequestHeader("authorization") String token, @PathVariable int id,
			@RequestBody Coupon coupon) {
		try {
			return new ResponseEntity<Coupon>(companyService.addCoupon(id, coupon), HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/coupon")
	public ResponseEntity<Coupon> getOneCoupon(@RequestHeader("authorization") String token,
			@RequestParam("id") int couponId) {
		try {
			return new ResponseEntity<Coupon>(companyService.getOneCoupon(couponId), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@DeleteMapping("/delete/coupon")
	public ResponseEntity<Coupon> deleteCoupon(@RequestHeader("authorization") String token,
			@RequestParam("id") int couponId) {
		try {
			return new ResponseEntity<Coupon>(companyService.deleteCoupon(couponId), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/{id}/get/coupon/all")
	public List<Coupon> getAllCompanyCoupons(@RequestHeader("authorization") String token, @PathVariable int id) {
		try {
			return companyService.getAllCompanyCoupons(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/{id}/get/coupon/all/{category}")
	public List<Coupon> getAllCompanyCoupons(@RequestHeader("authorization") String token, @PathVariable int id,
			@PathVariable("category") Category couponCategory) {
		try {
			return companyService.getAllCompanyCoupons(id, couponCategory);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/{id}/get/coupon/all/{maxPrice}")
	public List<Coupon> getAllCompanyCoupons(@RequestHeader("authorization") String token, @PathVariable int id,
			@PathVariable("maxPrice") double maxCouponPrice) {
		try {
			return companyService.getAllCompanyCoupons(id, maxCouponPrice);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping("/{id}/update/coupon")
	public ResponseEntity<Coupon> updateCoupon(@RequestHeader("authorization") String token, @PathVariable int id,
			@RequestBody Coupon coupon) {
		try {
			return new ResponseEntity<Coupon>(companyService.updateCoupon(id, coupon), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/{id}/get/details")
	public ResponseEntity<Company> getCompanyDetails(@RequestHeader("authorization") String token,
			@PathVariable int id) {
		try {
			return new ResponseEntity<Company>(companyService.getCompanyDetailsWithCoupons(id), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}
