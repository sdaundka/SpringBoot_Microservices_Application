package com.userservice.resources;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.userservice.entity.User;
import com.userservice.repositories.UserRepository;

@RestController
@RequestMapping("/users")
public class UserResource {
	
	@Autowired
	private UserRepository userRepository;
	
	@GetMapping
	public List<User> getUsers() {
		List<User> userList = new ArrayList<>();
		userRepository.findAll().forEach(userList::add);
		return userList;
	}
	
	@PostMapping
	public User addUser(@RequestBody User user) {
		System.out.println("Saving the user");
		System.out.println(user);
		return userRepository.save(user);
	}
	
	@GetMapping("/{userName}/{email}")
	public User getUser(@PathVariable String userName, @PathVariable String email) {
		System.out.println("User Name : "+userName + " , Email : "+email);
		User user = userRepository.findByNameAndEmail(userName, email);
		if(user != null)
			System.out.println("User found : "+user);
		else
			System.out.println("No user found.");
		
		return user;
	}
	
	@GetMapping("/{userId}")
	public User getUser(@PathVariable int userId) {
		User user = null;
		System.out.println("Find the user with the Id : "+userId);
		Optional<User> userOptional = userRepository.findById(Integer.valueOf(userId));
		if(userOptional.isEmpty())
			System.out.println("No user found.");
		else
			user = userOptional.get();
		
		System.out.println("Returning the user details : "+user);
		return user;
	}

}
