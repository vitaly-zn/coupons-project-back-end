package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.enums.Category;
import app.core.services.CustomerService;

@RestController
@RequestMapping("/api/customer")
public class CustomerController {

	private CustomerService customerService;

	@Autowired
	public CustomerController(CustomerService customerService) {
		this.customerService = customerService;
	}

	@PutMapping("/{id}/purchase")
	public ResponseEntity<Coupon> purchaseCoupon(@RequestHeader("authorization") String token, @PathVariable int id,
			@RequestBody Coupon coupon) {
		try {
			return new ResponseEntity<Coupon>(customerService.purchaseCoupon(id, coupon), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/coupon")
	public ResponseEntity<Coupon> getOneCoupon(@RequestHeader("authorization") String token,
			@RequestParam("id") int couponId) {
		try {
			return new ResponseEntity<Coupon>(customerService.getOneCoupon(couponId), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/get/coupon/all")
	public List<Coupon> getAllAvailableCoupons(@RequestHeader("authorization") String token) {
		try {
			return customerService.getAllAvailableCoupons();
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/{id}/get/coupon/all")
	public List<Coupon> getAllCustomerCoupons(@RequestHeader("authorization") String token, @PathVariable int id) {
		try {
			return customerService.getAllCustomerCoupons(id);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/{id}/get/coupon/all/{category}")
	public List<Coupon> getAllCustomerCoupons(@RequestHeader("authorization") String token, @PathVariable int id,
			@PathVariable("category") Category couponCategory) {
		try {
			return customerService.getAllCustomerCoupons(id, couponCategory);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/{id}/get/coupon/all/{maxPrice}")
	public List<Coupon> getAllCustomerCoupons(@RequestHeader("authorization") String token, @PathVariable int id,
			@PathVariable("maxPrice") double maxCouponPrice) {
		try {
			return customerService.getAllCustomerCoupons(id, maxCouponPrice);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/{id}/get/details")
	public ResponseEntity<Customer> getCustomerDetails(@RequestHeader("authorization") String token,
			@PathVariable int id) {
		try {
			return new ResponseEntity<Customer>(customerService.getCustomerDetailsWithCoupons(id), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

}
