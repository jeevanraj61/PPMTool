package io.ppmtool.code.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import io.ppmtool.code.domain.User;
import io.ppmtool.code.exceptions.ProjectIdException;
import io.ppmtool.code.exceptions.UsernameDuplicateException;
import io.ppmtool.code.repositories.UserRepository;
import io.ppmtool.code.validator.UserValidator;

@Service
public class UserService {

	@Autowired
    private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder bryptPasswordEncoder;
	
	
	
	public User saveUser(User newUser) {
		
		try {
			
			newUser.setPassword(bryptPasswordEncoder.encode(newUser.getPassword()));
			
			//username has to be unique
			newUser.setUsername(newUser.getUsername());
			//make sure password and confirmpassword matches
			// we dont persist or show confirm password
			newUser.setConfirmPassword("");
			return userRepository.save(newUser);
		}catch(Exception e) {
			throw new UsernameDuplicateException("Username '" + newUser.getUsername() +
					"' already exists");
		}
		
	}
	
	
}
