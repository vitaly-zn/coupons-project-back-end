package app.core.beans;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import app.core.enums.ClientType;
import app.core.exceptions.CouponSystemException;
import app.core.services.AdminService;
import app.core.services.ClientService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;

/**
 * The class {@code LoginManager} provides functionality to detect type of
 * client and refers it to correct client service (to correct business logic in
 * coupon system application).
 * 
 * @author VItaly Zlobin
 *
 */
@Component
public class LoginManager {

	private ApplicationContext ctx;

	@Autowired
	public LoginManager(ApplicationContext ctx) {
		this.ctx = ctx;
	}

	/**
	 * Returns correct and ready {@code ClientService} if specified values are valid
	 * (validation succeeded), otherwise - {@code null}.
	 * 
	 * @param email      specified client email to login.
	 * @param password   specified client password to login.
	 * @param clientType specified client type.
	 * @return correct client service if validation succeeded, otherwise -
	 *         {@code null};
	 * @throws CouponSystemException if some connection issues was occurred while
	 *                               validation.
	 */
	public ClientService login(String email, String password, ClientType clientType) throws CouponSystemException {
		switch (clientType) {
		case ADMINISTRATOR:
			// TODO: try-catch /???/ return null
			AdminService adminService = ctx.getBean(AdminService.class);
			if (adminService.login(email, password))
				return adminService;
			break;
		case COMPANY:
			// TODO: try-catch /???/ return null
			CompanyService companyService = ctx.getBean(CompanyService.class);
			if (companyService.login(email, password))
				return companyService;
			break;
		case CUSTOMER:
			// TODO: try-catch /???/ return null
			CustomerService customerService = ctx.getBean(CustomerService.class);
			if (customerService.login(email, password))
				return customerService;
			break;
		}

		return null;
	}

}
