package io.ppmtool.code.web;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.ppmtool.code.domain.User;
import io.ppmtool.code.payload.JWTLoginSuccessResponse;
import io.ppmtool.code.payload.LoginRequest;
import io.ppmtool.code.security.JwtTokenProvider;
import io.ppmtool.code.security.SecurityConstants;
import io.ppmtool.code.service.MapErrorValidationService;
import io.ppmtool.code.service.UserService;
import io.ppmtool.code.validator.UserValidator;

@RestController
@RequestMapping("/api/users")
public class UserController {
	
	
	@Autowired
	private MapErrorValidationService mapErrorValidationService;
	
	@Autowired
	private UserService userService;

	@Autowired
	private UserValidator userValidator;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Autowired
	private JwtTokenProvider jwtTokenProvider;
	
	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest, 
			BindingResult result){
		
		ResponseEntity<?> errorMap = mapErrorValidationService.MapValidationService(result);

		if (errorMap != null)
			return errorMap;
		
		Authentication authentication = authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()
						));
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = SecurityConstants.TOKEN_PREFIX + jwtTokenProvider.generateToken(authentication);
		
		 return ResponseEntity.ok(new JWTLoginSuccessResponse(true, jwt));
	}
	
	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody User user, BindingResult result){
		
		
		userValidator.validate(user, result);
		//validate passwords match
		ResponseEntity<?> errorMap = mapErrorValidationService.MapValidationService(result);

		if (errorMap != null)
			return errorMap;
		
		User newUser = userService.saveUser(user);
		return new ResponseEntity<User>(newUser, HttpStatus.CREATED);
		
	}
	
}
