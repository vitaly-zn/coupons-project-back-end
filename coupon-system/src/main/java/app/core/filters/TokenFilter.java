package app.core.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import app.core.enums.ClientType;
import app.core.jwt.JwtUtil;

public class TokenFilter implements Filter {

	private JwtUtil jwtUtil;

	@Autowired
	public TokenFilter(JwtUtil jwtUtil) {
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest req = (HttpServletRequest) request;
		String url = req.getRequestURI();

		if (url.endsWith("/login")) {
			chain.doFilter(request, response);
			System.out.println("================ - Passed Login Filter - ================");
			return;
		}

		HttpServletResponse resp = (HttpServletResponse) response;
		String token = req.getHeader("token");

		System.out.println("In filter before if token: " + token);

		if (token != null) {
			if (jwtUtil.isTokenExpired(token)) {
				resp.sendError(HttpStatus.UNAUTHORIZED.value(), "you are not logged in!");
			}
			if (req.getRequestURI().contains("/admin")) {
				if (jwtUtil.extractUserType(token) == ClientType.ADMINISTRATOR) {
					System.out.println("ADMIN FILTER PASS-------------");
					chain.doFilter(request, response);
				} else {
					System.out.println("ADMIN FILTER FAILL-------------");
					resp.sendError(HttpStatus.UNAUTHORIZED.value(), "you are not an admin");
				}

			} else if (req.getRequestURI().contains("/company")) {
				if (jwtUtil.extractUserType(token) == ClientType.COMPANY) {
					System.out.println("COMPANY FILTER PASS-------------");
					chain.doFilter(request, response);
				} else {
					System.out.println("COMPANY FILTER FAILL-------------");
					resp.sendError(HttpStatus.UNAUTHORIZED.value(), "you are not a company");
				}
			} else if (req.getRequestURI().contains("/customer")) {
				if (jwtUtil.extractUserType(token) == ClientType.CUSTOMER) {
					System.out.println("CUSTOMER FILTER PASS-------------");
					chain.doFilter(request, response);
				} else {
					System.out.println("CUSTOMER FILTER FAILL-------------");
					resp.sendError(HttpStatus.UNAUTHORIZED.value(), "you are not a customer");
				}
			} else {
				System.out.println("LOGIN FILTER PASS-------------");
				chain.doFilter(request, response);
			}
		} else {

			String method = req.getMethod();
			String acrhHeader = req.getHeader("access-control-request-headers");

			if (acrhHeader != null && method.equals("OPTIONS")) {
				System.out.println("PREFLIGHT-------------");
				resp.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE");
				resp.setHeader("Access-Control-Allow-Origin", "*");
				resp.setHeader("Access-Control-Allow-Headers", "*");
				resp.sendError(HttpStatus.OK.value(), "preflight");
			} else {
				System.out.println("LOGIN FILTER FAILL-------------");
				resp.sendError(HttpStatus.UNAUTHORIZED.value(), "you are not logged in");
			}
		}
	}

}
