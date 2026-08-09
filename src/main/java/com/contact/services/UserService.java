package com.contact.services;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.contact.entity.User;
import com.contact.exceptions.EmailAlreadyExistsException;
import com.contact.exceptions.ResourceNotFoundException;
import com.contact.exceptions.UserNotFoundException;
import com.contact.repositories.UserRepository;

@Service
public class UserService {

	@Autowired
	private UserRepository userRepository;

	public User findById(int id) throws UserNotFoundException{
		return this.userRepository.findById(id).get();
	}

	public User addNewUser(User user) {
		if(this.userRepository.existsByEmail(user.getEmail())) {
			throw new EmailAlreadyExistsException("user with email : "+user.getEmail()+
					" already exists please login!");
		} else {
			return this.userRepository.save(user);
		}
	}

	public User findByEmail(String email) {

		User reqUser = this.userRepository.findByEmail(email);

		if(reqUser == null) {
			throw new UserNotFoundException("user with email : "+email+" doesn't exist");
		}

		return reqUser;
	}

	public boolean existsByEmail(String email) throws UserNotFoundException{
		return this.userRepository.existsByEmail(email);
	}


	public void updateLastLogin(int id) {

		User user = this.userRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException());
		user.setLastLogin(LocalDateTime.now());
		this.userRepository.saveAndFlush(user);
	}

	public void saveUser(User user) {
		this.userRepository.saveAndFlush(user);
	}

}
