package com.contact.services;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.contact.entity.Contact;
import com.contact.entity.User;
import com.contact.exceptions.NoContactsFoundException;
import com.contact.exceptions.ResourceNotFoundException;
import com.contact.exceptions.UserNotFoundException;
import com.contact.repositories.ContactRepository;
import com.contact.repositories.UserRepository;

@Service
public class ContactService {

	@Autowired
	private ContactRepository contactRepository;

	@Autowired
	private UserRepository userRepository;

	public List<Contact> allContacts() throws NoContactsFoundException{
		return this.contactRepository.findAll();
	}

	public List<Contact> contactsByUser(User user){
		if(!this.userRepository.existsByEmail(user.getEmail())) {
			throw new UserNotFoundException();
		} else {
			return this.contactRepository.findByUser(user);
		}
	}

	public List<Contact> contactsOfUserAddedInWeek(int userid , LocalDateTime week){
		return this.contactRepository.
				findByUserIdAndAddedOnAfterOrderByAddedOn(userid, week);
	}

	public Contact addContact(Contact contact) {

		return this.contactRepository.save(contact);
	}

	public String getImageUrlByContact(int id) {
		Contact contact = this.contactRepository.findById(id).orElseThrow(()->
			new ResourceNotFoundException("no contacts found"));
		return contact.getImage();
	}

	public void updateImage(int id , String imgUrl) {

		Contact contact = this.contactRepository.findById(id).orElseThrow(()->
			new ResourceNotFoundException("no contacts found"));

		contact.setImage(imgUrl);

		this.contactRepository.save(contact);
	}

	public Contact findById(int id) throws NoResourceFoundException{
		return this.contactRepository.findById(id).get();
	}

	public void saveContact(Contact contact) {
		this.contactRepository.saveAndFlush(contact);
	}

	public void deleteContact(int id) throws ResourceNotFoundException{
		this.contactRepository.deleteById(id);
	}

	public Page<Contact> getCurrentPage(int userid , Pageable pageable) {
		return this.contactRepository.findByUserId(userid , pageable);
	}

	public Page<Contact> searchContacts(int userid , String keyword , Pageable pageable){
		return this.contactRepository.searchContacts(userid, keyword, pageable);
	}

}

