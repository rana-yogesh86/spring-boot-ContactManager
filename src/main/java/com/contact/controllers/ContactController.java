package com.contact.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import com.contact.dto.ContactRequest;
import com.contact.entity.Contact;
import com.contact.entity.User;
import com.contact.services.ContactService;
import com.security.CustomUserDetails;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user/contacts")
public class ContactController {

	@Autowired
	private ContactService contactService;

	List<String> allowedFileTypes = List.of(
			"image/jpeg",
			"image/png",
			"image/webp",
			"application/octet-stream");

	@GetMapping
	public String contactsOfUser(@AuthenticationPrincipal CustomUserDetails userDetails,
			@RequestParam(defaultValue = "0") int page ,
			@RequestParam(defaultValue = "") String keyword,Model model) {

		User currentUser=userDetails.getUser();

		Pageable pageable = PageRequest.of(page, 3 , Sort.by("addedOn").descending());

		Page<Contact>  contacts;

		if(keyword == null || keyword.isBlank()) {
			contacts =  this.contactService.getCurrentPage(currentUser.getId()
					,pageable);
		}
		else {
			contacts = this.contactService.searchContacts(currentUser.getId(), keyword, pageable);
		}

		model.addAttribute("contactsOfUser", this.contactService.contactsByUser(currentUser));
		model.addAttribute("contacts", contacts);
		model.addAttribute("currentPage", page);
		model.addAttribute("totalPages", contacts.getTotalPages());
		model.addAttribute("activePage", "contacts");
		model.addAttribute("keyword", keyword);
		return "contacts/contact-list";
	}

	@GetMapping("/add")
	public String addContact(Model model) {

		model.addAttribute("contact", new ContactRequest());
		model.addAttribute("formAction", "/user/contacts/save");
		return "contacts/add-contact";
	}

	@PostMapping("/save")
	public String saveContact(@Valid @ModelAttribute("contact") ContactRequest contact,
			BindingResult bindingResult , @AuthenticationPrincipal CustomUserDetails userDetails
			, Model model) throws IOException {

		System.out.println(contact.toString());

		MultipartFile newImage = contact.getImage();
		Path newImagePath = null;

		if(!newImage.isEmpty()) {
			if(!allowedFileTypes.contains(newImage.getContentType())) {
				bindingResult.rejectValue(
						"image",
						"invalid.image",
						"upload only .jpeg/.png/.webp formats");
				return "contacts/add-contact";
			}

			else {
				String imgName = UUID.randomUUID()+"_"+newImage.getOriginalFilename();

				newImagePath = Path.of("uploads","contacts").resolve(imgName);

				Files.createDirectories(newImagePath.getParent());
				Files.copy(newImage.getInputStream(),newImagePath, StandardCopyOption.REPLACE_EXISTING);
			}

		} else {
			newImagePath=Path.of("uploads","default_user.png");
		}


		if(!bindingResult.hasErrors()) {
			Contact newContact = new Contact();
			newContact.setName(contact.getName());
			newContact.setEmail(contact.getEmail());
			newContact.setContactNumber(contact.getContactNumber());
			newContact.setWork(contact.getWork());
			newContact.setAddress(contact.getAddress());
			newContact.setDescription(contact.getDescription());
			newContact.setImage(newImagePath.toString());
			newContact.setUser(userDetails.getUser());

			this.contactService.addContact(newContact);

			model.addAttribute("successMessage", "Contact saved successfully");

			List<Contact>  contacts =  this.contactService.contactsByUser(userDetails.getUser());
			model.addAttribute("contacts", contacts);
			return "contacts/contact-list";
		}
		else {
			model.addAttribute("errorMessage", "Something,went wrong!");
			return "contacts/add-contact";
		}

	}

	@GetMapping("/{id}")
	public String viewContact(@PathVariable int id , Model model) throws NoResourceFoundException {

		Contact contact= this.contactService.findById(id);
		model.addAttribute("contact",contact);
		return "contacts/contact-profile";
	}

	@GetMapping("/{id}/edit")
	public String editContact(@PathVariable int id , Model model) throws NoResourceFoundException {

		Contact contact= this.contactService.findById(id);

		ContactRequest contactRequest = new ContactRequest();

		contactRequest.setId(contact.getId());
		contactRequest.setName(contact.getName());
		contactRequest.setEmail(contact.getEmail());
		contactRequest.setWork(contact.getWork());
		contactRequest.setAddress(contact.getAddress());
		contactRequest.setDescription(contact.getDescription());
		contactRequest.setCurrentImage(contact.getImage());
		contactRequest.setContactNumber(contact.getContactNumber());

		model.addAttribute("contact", contactRequest);

		model.addAttribute("formAction", "/user/contacts/" + id + "/update");
		return "contacts/add-contact";
	}

	@PostMapping("/{id}/update")
	public String updateContact(@Valid @ModelAttribute("contact") ContactRequest contactRequest , BindingResult bindingResult ,
			@AuthenticationPrincipal CustomUserDetails userDetails,Model model) throws Exception {

		System.out.println(contactRequest.getCurrentImage());
		//image size and format
		if(bindingResult.hasErrors()) {
			return "contacts/add-contact";
		}

		MultipartFile updatedImage = contactRequest.getImage();

		if(!allowedFileTypes.contains(updatedImage.getContentType())) {
			bindingResult.rejectValue(
					"image", "invalid.image","invalid image format");

		}

		String imageFilename = UUID.randomUUID()+"_"+
				updatedImage.getOriginalFilename();

		Contact updatedContact = this.contactService.findById(contactRequest.getId());

		if(!updatedImage.isEmpty()) {

			Path uploadPath = Path.of("uploads","contacts");
			Files.createDirectories(uploadPath);

			Path updatedImagePath = uploadPath.resolve(imageFilename);

			Files.copy(updatedImage.getInputStream(),
					updatedImagePath,StandardCopyOption.REPLACE_EXISTING);

			updatedContact.setImage(updatedImagePath.toString());
		} else {
			updatedContact.setImage(contactRequest.getCurrentImage());
		}



		updatedContact.setName(contactRequest.getName());
		updatedContact.setEmail(contactRequest.getEmail());
		updatedContact.setContactNumber(contactRequest.getContactNumber());
		updatedContact.setWork(contactRequest.getWork());
		updatedContact.setAddress(contactRequest.getAddress());
		updatedContact.setDescription(contactRequest.getDescription());
		updatedContact.setUser(userDetails.getUser());

		this.contactService.saveContact(updatedContact);
		List<Contact>  contacts =  this.contactService.contactsByUser(userDetails.getUser());
		model.addAttribute("contacts", contacts);

		return "contacts/contact-list";
	}

	@PostMapping("/{id}/delete")
	public String deleteContact(@PathVariable int id ,Model model ,
			@AuthenticationPrincipal CustomUserDetails userDetails) {

		this.contactService.deleteContact(id);

		List<Contact>  contacts =  this.contactService.contactsByUser(userDetails.getUser());
		model.addAttribute("contacts", contacts);

		return "contacts/contact-list";
	}

}
