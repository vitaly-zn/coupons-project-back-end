package app.core;

import java.time.LocalDate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import app.core.beans.LoginManager;
import app.core.entities.Company;
import app.core.entities.Coupon;
import app.core.entities.Customer;
import app.core.enums.Category;
import app.core.enums.ClientType;
import app.core.filters.TokenFilter;
import app.core.jwt.JwtUtil;
import app.core.services.AdminService;
import app.core.services.CompanyService;
import app.core.services.CustomerService;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class CouponSystemApplication {

	public static void main(String[] args) {

		SpringApplication.run(CouponSystemApplication.class, args);
	}

	@Bean
	public WebMvcConfigurer corsConfigurer() {
		return new WebMvcConfigurer() {
			@Override
			public void addCorsMappings(CorsRegistry registry) {
				registry.addMapping("/api/**")

						.allowedOrigins("http://localhost:3000")

						.allowedHeaders("*")

						.maxAge(3600)

						.allowedMethods("GET", "POST", "PUT", "DELETE");

			}
		};
	}

	@Bean
	public FilterRegistrationBean<TokenFilter> tokenFilterRegistration(JwtUtil jwtUtil) {
		FilterRegistrationBean<TokenFilter> filterRegistrationBean = new FilterRegistrationBean<>();
		TokenFilter tokenFilter = new TokenFilter(jwtUtil);
		filterRegistrationBean.setFilter(tokenFilter);
		filterRegistrationBean.addUrlPatterns("/api/**");
		return filterRegistrationBean;
	}

	/**
	 * Initializing DB with data.
	 * 
	 * @param adminService
	 * @param companyService
	 * @param customerService
	 * @return
	 */
	@Bean
	CommandLineRunner initDB(LoginManager manager) {
		CommandLineRunner runner = new CommandLineRunner() {

			@Override
			public void run(String... args) throws Exception {

				////////////////////////////// - Administrator - ///////////////////////////////
				AdminService adminService = (AdminService) manager.login("admin@admin.com", "admin",
						ClientType.ADMINISTRATOR);
				Company company1 = new Company(0, "FORD", "ford@mail.com", "fordp");
				Company company2 = new Company(0, "BUG", "bug@mail.co.il", "bugp");

				Customer customer1 = new Customer(0, "David", "Davidof", "david@mail.com", "david");
				Customer customer2 = new Customer(0, "Ruth", "Levi", "ruth@mail.com", "ruth");

				company1 = adminService.addCompany(company1);
				company2 = adminService.addCompany(company2);
				customer1 = adminService.addCustomer(customer1);
				customer2 = adminService.addCustomer(customer2);

				////////////////////////////// - Company - ///////////////////////////////
				LocalDate now = LocalDate.now();
				Coupon coupon1 = new Coupon(0, 0, Category.SPORT, "FORD TITLE", "FORD description",
						LocalDate.of(2020, 1, 1), LocalDate.of(now.plusDays(3).getYear(), 12, 31), 3, 250000.99,
						"FORD image");
				Coupon coupon2 = new Coupon(0, 0, Category.ELECTRICITY, "BUG TITLE", "BUG description",
						LocalDate.of(2021, 3, 15), LocalDate.of(now.getYear(), now.getMonth(), 20), 5, 650.99,
						"BUG image");
				Coupon coupon3 = new Coupon(0, 0, Category.ELECTRICITY, "expired", "expired description",
						LocalDate.of(2020, 3, 15), LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth()), 22,
						11.99, "expired image");

				CompanyService companyService = (CompanyService) manager.login("ford@mail.com", "fordp",
						ClientType.COMPANY);
				Company currentCompany = companyService.getCompanyByEmailAndPassword("ford@mail.com", "fordp");
				coupon1 = companyService.addCoupon(currentCompany.getId(), coupon1);

				companyService = (CompanyService) manager.login("bug@mail.co.il", "bugp", ClientType.COMPANY);
				currentCompany = companyService.getCompanyByEmailAndPassword("bug@mail.co.il", "bugp");
				coupon2 = companyService.addCoupon(currentCompany.getId(), coupon2);
				coupon3 = companyService.addCoupon(currentCompany.getId(), coupon3);

				////////////////////////////// - Customer - ///////////////////////////////
				CustomerService customerService = (CustomerService) manager.login("david@mail.com", "david",
						ClientType.CUSTOMER);
				Customer currentCustomer = customerService.getCustomerByEmailAndPassword("david@mail.com", "david");
				coupon1 = customerService.purchaseCoupon(currentCustomer.getId(), coupon1);
				coupon2 = customerService.purchaseCoupon(currentCustomer.getId(), coupon2);
				coupon3 = customerService.purchaseCoupon(currentCustomer.getId(), coupon3);

				customerService = (CustomerService) manager.login("ruth@mail.com", "ruth", ClientType.CUSTOMER);
				currentCustomer = customerService.getCustomerByEmailAndPassword("ruth@mail.com", "ruth");
				coupon2 = customerService.purchaseCoupon(currentCustomer.getId(), coupon2);
				coupon1 = customerService.purchaseCoupon(currentCustomer.getId(), coupon1);
				coupon3 = customerService.purchaseCoupon(currentCustomer.getId(), coupon3);
			}
		};
		return runner;
	}

}
