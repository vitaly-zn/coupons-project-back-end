package app.core.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import app.core.beans.LoginManager;
import app.core.entities.Company;
import app.core.entities.Customer;
import app.core.enums.ClientType;
import app.core.exceptions.CouponSystemException;
import app.core.jwt.JwtUtil;
import app.core.jwt.LoginModel;
import app.core.jwt.UserDetails;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;

@RestController
public class LoginController {

	private LoginManager loginManager;
	private JwtUtil jwtUtil;

	@Autowired
	public LoginController(LoginManager loginManager, JwtUtil jwtUtil) {
		this.loginManager = loginManager;
		this.jwtUtil = jwtUtil;
	}

	@PostMapping("/api/login")
	public UserDetails login(@RequestBody LoginModel loginModel) {
		try {
			String email = loginModel.email;
			String password = loginModel.password;
			ClientType clientType = ClientType.valueOf(loginModel.clientType);
			ClientService currentClient = loginManager.login(email, password, clientType);
			System.out.println("Login Succeeded!");

			if (currentClient == null)
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid email or password!");

			String clientName = null;
			int clientId = -1;

			if (currentClient instanceof AdminService) {
				clientName = "Administrator";
			} else if (currentClient instanceof CompanyService) {
				Company company = ((CompanyService) currentClient).getCompanyByEmailAndPassword(email, password);
				clientId = company.getId();
				clientName = company.getName();
			} else {
				Customer customer = ((CustomerService) currentClient).getCustomerByEmailAndPassword(email, password);
				clientId = customer.getId();
//				clientName = customer.getFirstName().concat(" ").concat(customer.getLastName());
				clientName = customer.getFirstName();
			}

			UserDetails userDetails = new UserDetails(clientId, clientName, email, clientType);

			String token = jwtUtil.generateToken(userDetails);
			userDetails.token = token;

			System.out.println("\n===================== Client Service Login =====================");
			System.out.println("Email: " + email);
			System.out.println("Password: " + password);
			System.out.println("Client Type: " + clientType);
			System.out.println("Client Service: " + currentClient == null ? null : currentClient.getClass().getName());
			System.out.println("Token: " + token);

			System.out.println(" - Login Succeeded! - ");
			return userDetails;
		} catch (CouponSystemException e) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
		}
	}

}
