package com.contact.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.contact.entity.User;
import com.contact.entity.UserRole;
import com.contact.repositories.UserRepository;

@Service
public class AdminService {

	@Autowired
	private UserRepository userRepository;

	public Page<User> allUsers(UserRole role , Pageable pageable){
		return this.userRepository.findAllByRole(role, pageable );
	}

	public int countByRole(UserRole role) {
		return this.userRepository.countByRole(role);
	}

	public int countByActive(boolean isActive) {
		return (int) this.userRepository.countByIsActiveAndRole(isActive , UserRole.USER);
	}

}
