package com.contact.repositories;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.contact.entity.Contact;
import com.contact.entity.User;

public interface ContactRepository extends JpaRepository<Contact, Integer> {
	public List<Contact> findByUser(User user);

	public List<Contact> findByUserIdAndAddedOnAfterOrderByAddedOn(int userid , LocalDateTime date);

	public Page<Contact> findByUserId(int userid , Pageable pageable);

	@Query("SELECT c FROM Contact c where c.user.id = :userId AND ( "
			+ "LOWER(c.name) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "
			+ "LOWER(c.email) LIKE LOWER(CONCAT('%',:keyword,'%')) OR "
			+ "c.contactNumber LIKE LOWER(CONCAT('%',:keyword,'%'))"
			+ ")")
	public Page<Contact> searchContacts(@Param("userId") int userid ,
			@Param("keyword") String keyword , Pageable pageable);
}
