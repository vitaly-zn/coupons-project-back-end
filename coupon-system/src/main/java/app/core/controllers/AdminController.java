package app.core.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.services.AdminService;

@RestController
@RequestMapping("/api/admin")
public class AdminController {

	private AdminService adminService;

	@Autowired
	public AdminController(AdminService adminService) {
		this.adminService = adminService;
	}

	@PostMapping("/add/company")
	public ResponseEntity<Company> addCompany(@RequestHeader("authorization") String token,
			@RequestBody Company company) {
		try {
			return new ResponseEntity<Company>(adminService.addCompany(company), HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/company")
	public ResponseEntity<Company> getCompany(@RequestHeader("authorization") String token,
			@RequestParam("id") int companyId) {
		try {
			return new ResponseEntity<Company>(adminService.getOneCompanyWithCoupons(companyId), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/get/company/all")
	public ResponseEntity<List<Company>> getAllCompanies(@RequestHeader("authorization") String token) {
		try {
			return new ResponseEntity<List<Company>>(adminService.getAllCompanies(), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping("/update/company")
	public ResponseEntity<Company> updateCompany(@RequestHeader("authorization") String token,
			@RequestBody Company company) {
		try {
			return new ResponseEntity<Company>(adminService.updateComapany(company), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete/company")
	public ResponseEntity<Company> deleteCompany(@RequestHeader("authorization") String token,
			@RequestParam("id") int companyId) {
		try {
			return new ResponseEntity<Company>(adminService.deleteCompany(companyId), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}
	}

	@PostMapping("/add/customer")
	public ResponseEntity<Customer> addCustomer(@RequestHeader("authorization") String token,
			@RequestBody Customer customer) {
		try {
			return new ResponseEntity<Customer>(adminService.addCustomer(customer), HttpStatus.CREATED);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@GetMapping("/get/customer")
	public ResponseEntity<Customer> getCustomer(@RequestHeader("authorization") String token,
			@RequestParam("id") int customerId) {
		try {
			return new ResponseEntity<Customer>(adminService.getOneCustomerWithCoupons(customerId), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@GetMapping("/get/customer/all")
	public ResponseEntity<List<Customer>> getAllCustomers(@RequestHeader("authorization") String token) {
		try {
			return new ResponseEntity<List<Customer>>(adminService.getAllCustomers(), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
		}
	}

	@PutMapping("/update/customer")
	public ResponseEntity<Customer> updateCustomer(@RequestHeader("authorization") String token,
			@RequestBody Customer customer) {
		try {
			return new ResponseEntity<Customer>(adminService.updateCustomer(customer), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, e.getMessage());
		}
	}

	@DeleteMapping("/delete/customer")
	public ResponseEntity<Customer> deleteCustomer(@RequestHeader("authorization") String token,
			@RequestParam("id") int customerId) {
		try {
			return new ResponseEntity<Customer>(adminService.deleteCustomer(customerId), HttpStatus.OK);
		} catch (Exception e) {
			throw new ResponseStatusException(HttpStatus.NO_CONTENT, e.getMessage());
		}
	}

}
