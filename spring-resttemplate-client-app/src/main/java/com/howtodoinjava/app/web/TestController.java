package com.howtodoinjava.app.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.howtodoinjava.app.model.LoginRequestDTO;
import com.howtodoinjava.app.model.LoginResponseDTO;
import com.howtodoinjava.app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
public class TestController {

	@Autowired
	RestTemplate restTemplate;

	//private static final String REGISTRATION_URL = "http://localhost:8081/register";
	private static final String AUTHENTICATION_URL = "http://localhost:8081/store/auth/login";
	private static final String USERS_URL = "http://localhost:8081/store/users";

	@RequestMapping(value = "/getListUsers", method = RequestMethod.GET)
	public String getResponse() throws JsonProcessingException {  //List<User>

		String response = null;
		List<User> users = null;

		try {
				// create user authentication object
			LoginRequestDTO authenticationUser = getAuthenticationUser();
				// convert the user authentication object to JSON
				String authenticationBody = getBody(authenticationUser);
				// create headers specifying that it is JSON request
				HttpHeaders authenticationHeaders = getHeaders();
				HttpEntity<String> authenticationEntity = new HttpEntity<String>(authenticationBody,
						authenticationHeaders);

				// Authenticate User and get JWT
				ResponseEntity<LoginResponseDTO> authenticationResponse = restTemplate.exchange("/auth/login",
						HttpMethod.POST, authenticationEntity, LoginResponseDTO.class);
					
				// if the authentication is successful		
				System.out.println(authenticationResponse.getStatusCode().equals(HttpStatus.OK));
				// if the authentication is successful		
				if (authenticationResponse.getStatusCode().equals(HttpStatus.OK)) {
					String token = "Bearer " + authenticationResponse.getBody().getAccessToken();
					HttpHeaders headers = getHeaders();
					headers.set("Authorization", token);
					HttpEntity<String> jwtEntity = new HttpEntity<String>(headers);
					// Use Token to get Response
					ResponseEntity<String> helloResponse = restTemplate.exchange("/users", HttpMethod.GET, jwtEntity,
							String.class);
					//ResponseEntity<List> helloResponse = restTemplate.exchange("/users", HttpMethod.GET, jwtEntity,
					//		List.class);
					if (helloResponse.getStatusCode().equals(HttpStatus.OK)) {
						//users = helloResponse.getBody();
						//System.out.println(users);

						response = helloResponse.getBody();

					}
				}
				
			//}

				
		}catch(Exception ex)
		{
			System.out.println("exception");
			
		}
		//return users;
		return response;
		
	}

	private LoginRequestDTO getAuthenticationUser() {
		LoginRequestDTO loginRequestDTO = new LoginRequestDTO();
		loginRequestDTO.setUsername("jhon@gmail.com");
		loginRequestDTO.setPassword("123456");
		return loginRequestDTO;
	}

	private HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.set("Content-Type", MediaType.APPLICATION_JSON_VALUE);
		headers.set("Accept", MediaType.APPLICATION_JSON_VALUE);
		return headers;
	}

	private String getBody(final LoginRequestDTO user) throws JsonProcessingException {
		return new ObjectMapper().writeValueAsString(user);
	}
}