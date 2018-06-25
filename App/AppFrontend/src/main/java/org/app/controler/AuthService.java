package org.app.controler;

import java.math.BigInteger;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.Cookie;

import org.app.model.entity.Account;

import com.vaadin.server.VaadinService;

@RequestScoped
public class AuthService {

	@Inject
	private AccountService accountService;

	@Inject
	private SessionService sessionService;
	
	@Inject
	private EncryptService encryptService;

	private Account account;
	private String MessageForAuthentication = "Login Succesfull";

	private final String COOKIE_NAME = "remember-me";
	private final int COOKIE_MAX_AGE_DAYS = 30;

	private static Map<String, String> remembered = new HashMap<>();
	private static SecureRandom random = new SecureRandom();

	private boolean validateUser(String username) {
		account = new Account();

		try {
			account = accountService.findByUserName(username);
		} catch (Exception e) {
			MessageForAuthentication = "Username " + username + " not exist" ;
			return false;
		}
		return true;
	}

	private boolean validatePassword(String password) {

		if (encryptService.getEncoder().matches(password, account.getPassword())) {
			return true;
		} else {
			MessageForAuthentication = "Password not correct";
			return false;
		}

	}

	public boolean validateAccount(String username, String password, boolean rememberMe) {
		boolean validUser = false;
		boolean validPassword = false;
		boolean authentic = false;

		validUser = validateUser(username);
		
		if (validUser) {
			validPassword = validatePassword(password);
		}

		if (validUser == true && validPassword == true) {
			authentic = true;
		}

		if (authentic) {
			sessionService.setCurrentUser(account);
			if (rememberMe) {
				rememberUser(username);
			} else {
				removeRememberedUser();	
			}
		}

		return authentic;
	}

	public boolean isRememberedUser() {
		Optional<Cookie> cookie = getCookie();
		return cookie.isPresent() && remembered.containsKey(cookie.get().getValue());
	}

	public void removeRememberedUser() {
		Optional<Cookie> cookie = getCookie();

		if (cookie.isPresent()) {
			remembered.remove(cookie.get().getValue());
			saveCookie("", 0);
		}
	}

	private void rememberUser(String username) {
		String randomKey = new BigInteger(130, random).toString(32);
		remembered.put(randomKey, username);
		saveCookie(randomKey, 60 * 60 * 24 * COOKIE_MAX_AGE_DAYS);
	}

	private void saveCookie(String value, int age) {
		Cookie cookie = new Cookie(COOKIE_NAME, value);
		cookie.setPath("/");
		cookie.setMaxAge(age);
		VaadinService.getCurrentResponse().addCookie(cookie);
	}

	private Optional<Cookie> getCookie() {
		Cookie[] cookies = VaadinService.getCurrentRequest().getCookies();
		return Arrays.stream(cookies).filter(c -> COOKIE_NAME.equals(c.getName())).findFirst();
	}

	public String getMessageForAuthentication() {
		return MessageForAuthentication;
	}

	public Account getAccount() {
		return account;
	}

	public void setAccount(Account account) {
		this.account = account;
	}

}
