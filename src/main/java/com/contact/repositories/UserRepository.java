package com.contact.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.contact.entity.User;
import com.contact.entity.UserRole;



public interface UserRepository extends JpaRepository<User, Integer> {
	public boolean existsByEmail(String email);

	public User findByEmail(String email);

	public Page<User> findAllByRole(UserRole role  , Pageable pageable);

	public int countByRole(UserRole role);

	public long countByIsActiveAndRole(boolean isActive , UserRole role);

}
