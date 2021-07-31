package app.core.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import app.core.entities.Company;

public interface CompanyRepository extends JpaRepository<Company, Integer> {

	/**
	 * Retrieves entity by its email and password.
	 * 
	 * @param email    specified entity email to check.
	 * @param password specified entity password to check.
	 * @return entity with specified parameters if found.
	 */
	Company findByEmailIgnoreCaseAndPassword(String email, String password);

	/**
	 * Retrieves entity with specified name or email if found.
	 * 
	 * @param name  specified entity name to check.
	 * @param email specified entity email to check.
	 * @return entity with specified parameters if found.
	 */
	List<Company> findByNameOrEmailIgnoreCase(String name, String email);

}
