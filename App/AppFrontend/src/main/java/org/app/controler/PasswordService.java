package org.app.controler;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordService {

	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(); 

	public String encodePassword(String password) {
		return encoder.encode(password);
	}
	
	public Boolean validatePassword(String password, String passwordFromDatabase) {
		return encoder.matches(password, passwordFromDatabase); 
	}

}
