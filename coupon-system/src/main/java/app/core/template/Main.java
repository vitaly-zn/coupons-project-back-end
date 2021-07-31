package app.core.template;

import java.net.URI;
import java.util.List;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.enums.ClientType;

public class Main {

	public static void main(String[] args) {
		Main main = new Main();
		RestTemplate rt = new RestTemplate();

		System.out.println("in main");

		try {

			{
				String email = "admin@admin.com";
				String password = "admin";
				ClientType clientType = ClientType.ADMINISTRATOR;
				String url = "http://localhost:8080/login/";
				System.out.println("url: " + url);

				System.out.println("URI Template: " + rt.getUriTemplateHandler().toString());

//				ObjectMapper om = new ObjectMapper();
//				om.setVisibility(PropertyAccessor.FIELD, Visibility.ANY);
//				String dtoAsString = om.writeValueAsString(new ClientService)

//				ResponseEntity<ClientService> entity = rt.postForEntity(new URI(url), null, ClientService.class);
				ResponseEntity<String> entity = rt.postForEntity(url, null, String.class, email, password, clientType);
				System.out.println(entity.getStatusCode());
				System.out.println(entity.getBody());
			}

			///////////////////////////////////////////////////////////////////
			///////////////////////////// - ADMIN - ///////////////////////////
			///////////////////////////////////////////////////////////////////

			// add Company
//			main.addCompany(rt, new Company(0, "Toyota", "toyota@mail.com", "tpass"));

			// get Company
//			main.getCompany(rt, 4);

			// get All Companies
//			main.getAllCompanies(rt);

			// update Company
//			main.updateCompany(rt, new Company(4, "TEMP", "TEMP@mail.com", "TEMPpass"));

			// delete Company
//			main.deleteCompany(rt, 7);

			// add Customer
//			main.addCustomer(rt, new Customer(0, "Moishe", "Ufnik", "m-u@mail.com", "mp"));

			// get Customer
//			main.getCustomer(rt, 4);

			// get All Customers
//			main.getAllCustomers(rt);

			// update Customer
//			main.updateCustomer(rt, new Customer(4, "TEMP-F", "TEMP-L", "TEMP-F-L@mail.com", "TEMPpass"));

			// delete Customer
//			main.deleteCustomer(rt, 7);

			///////////////////////////////////////////////////////////////////
			//////////////////////////// - Company - //////////////////////////
			///////////////////////////////////////////////////////////////////

			// add Coupon

			// get One Coupon

			// delete Coupon

			// get all Company Coupons

			// get all Company Coupons by category

			// get all Company Coupons by max price

			// update Coupon

			// get Company Details

			///////////////////////////////////////////////////////////////////
			/////////////////////////// - Customer - //////////////////////////
			///////////////////////////////////////////////////////////////////

			// Purchase Coupon

			// Get All Available Coupons

			// Get All Customer Coupons

			// Get All Customer Coupons by Category

			// Get All Customer Coupons by max price

			// Get Customer Details
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
//			e.printStackTrace();
		}
	}

	void addCompany(RestTemplate rt, Company company) {
		System.out.println("\n######################### - Add Company - ##########################");
		String url = "http://localhost:8080/add/company";
		System.out.println("URL: " + url);

		System.out.println("Company to Add: " + company);

		try {
			ResponseEntity<Company> responseEntity = rt.postForEntity(new URI(url), company, Company.class);
			System.out.println("Response Status: " + responseEntity.getStatusCode());
			System.out.println("Response Body: " + responseEntity.getBody());
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}

	void getCompany(RestTemplate rt, int id) {
		System.out.println("\n######################### - Get Company - ##########################");
		String url = "http://localhost:8080/get/company?id=" + id;
		System.out.println("URL: " + url);

		try {
			ResponseEntity<Company> entity = rt.getForEntity(new URI(url), Company.class);
			System.out.println("Response Status: " + entity.getStatusCode());
			System.out.println("Response Body: " + entity.getBody());
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}

	void getAllCompanies(RestTemplate rt) {
		System.out.println("\n######################### - Get All Companies - ##########################");
		String url = "http://localhost:8080/get/company/all";
		System.out.println("URL: " + url);

		ParameterizedTypeReference<List<Company>> typeReference = new ParameterizedTypeReference<List<Company>>() {
		};

		try {
			ResponseEntity<List<Company>> entity = rt.exchange(new URI(url), HttpMethod.GET, null, typeReference);

			System.out.println("Response Status: " + entity.getStatusCode());

			if (entity.getBody().isEmpty())
				System.out.println("There are No Companies to Display.");
			else {
				System.out.println(" - Companies List - ");
				entity.getBody().forEach(System.out::println);
			}
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}

	void updateCompany(RestTemplate rt, Company company) {
		System.out.println("\n######################### - Update Company - ##########################");
		String url = "http://localhost:8080/update/company";
		System.out.println("URL: " + url);

		try {
			RequestEntity<Company> requestEntity = new RequestEntity<Company>(company, HttpMethod.PUT, new URI(url));
			ResponseEntity<Company> entity = rt.exchange(requestEntity, Company.class);
			System.out.println("Response Status: " + entity.getStatusCode());
			System.out.println("Response Body: " + entity.getBody());
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}

	void deleteCompany(RestTemplate rt, int id) {
		System.out.println("\n######################### - Delete Company - ##########################");
		String url = "http://localhost:8080/delete/company?id=" + id;
		System.out.println("URL: " + url);

		try {
			ResponseEntity<Company> entity = rt.exchange(new URI(url), HttpMethod.DELETE, null, Company.class);
			System.out.println("Response Status: " + entity.getStatusCode());
			System.out.println("Response Body: " + entity.getBody());
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}

	void addCustomer(RestTemplate rt, Customer customer) {
		System.out.println("\n######################### - Add Customer - ##########################");
		String url = "http://localhost:8080/add/customer";
		System.out.println("URL: " + url);

		System.out.println("Company to Add: " + customer);

		try {
			ResponseEntity<Customer> responseEntity = rt.postForEntity(new URI(url), customer, Customer.class);
			System.out.println("Response Status: " + responseEntity.getStatusCode());
			System.out.println("Response Body: " + responseEntity.getBody());
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}

	void getCustomer(RestTemplate rt, int id) {
		System.out.println("\n######################### - Get Customer - ##########################");
		String url = "http://localhost:8080/get/customer?id=" + id;
		System.out.println("URL: " + url);

		try {
			ResponseEntity<Customer> entity = rt.getForEntity(new URI(url), Customer.class);
			System.out.println("Response Status: " + entity.getStatusCode());
			System.out.println("Response Body: " + entity.getBody());
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}

	void getAllCustomers(RestTemplate rt) {
		System.out.println("\n######################### - Get All Customers - ##########################");
		String url = "http://localhost:8080/get/customer/all";
		System.out.println("URL: " + url);

		ParameterizedTypeReference<List<Customer>> typeReference = new ParameterizedTypeReference<List<Customer>>() {
		};

		try {
			ResponseEntity<List<Customer>> entity = rt.exchange(new URI(url), HttpMethod.GET, null, typeReference);

			System.out.println("Response Status: " + entity.getStatusCode());

			if (entity.getBody().isEmpty())
				System.out.println("There are No Customers to Display.");
			else {
				System.out.println(" - Customers List - ");
				entity.getBody().forEach(System.out::println);
			}
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}

	void updateCustomer(RestTemplate rt, Customer customer) {
		System.out.println("\n######################### - Update Customer - ##########################");
		String url = "http://localhost:8080/update/customer";
		System.out.println("URL: " + url);

		try {
			RequestEntity<Customer> requestEntity = new RequestEntity<Customer>(customer, HttpMethod.PUT, new URI(url));
			ResponseEntity<Customer> entity = rt.exchange(requestEntity, Customer.class);
			System.out.println("Response Status: " + entity.getStatusCode());
			System.out.println("Response Body: " + entity.getBody());
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}

	void deleteCustomer(RestTemplate rt, int id) {
		System.out.println("\n######################### - Delete Customer - ##########################");
		String url = "http://localhost:8080/delete/customer?id=" + id;
		System.out.println("URL: " + url);

		try {
			ResponseEntity<Customer> entity = rt.exchange(new URI(url), HttpMethod.DELETE, null, Customer.class);
			System.out.println("Response Status: " + entity.getStatusCode());
			System.out.println("Response Body: " + entity.getBody());
		} catch (Exception e) {
			System.err.println("=============================");
			System.err.println(e.getMessage());
		}
	}
}
