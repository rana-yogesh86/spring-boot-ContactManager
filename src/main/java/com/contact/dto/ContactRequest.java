package com.contact.dto;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class ContactRequest {

	private int id;

	@NotBlank(message = "can't be empty")
	@Size(min = 5 ,max=20, message = "must be [5-20] characters!")
	private String name;

	@NotBlank
	@Size(min = 10 , max=10, message = "invalid contact no.!")
	private String contactNumber;

	@NotBlank
	@Email
	private String email;
	private String work;
	private String address;
	private String description;
	private MultipartFile image;
	private String currentImage;

	public ContactRequest() {
		// TODO Auto-generated constructor stub
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getWork() {
		return work;
	}

	public void setWork(String work) {
		this.work = work;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public MultipartFile getImage() {
		return image;
	}

	public void setImage(MultipartFile image) {
		this.image = image;
	}

	public String getCurrentImage() {
		return currentImage;
	}

	public void setCurrentImage(String currentImage) {
		this.currentImage = currentImage;
	}

	@Override
	public String toString() {
		return "ContactRequest [id=" + id + ", name=" + name + ", contactNumber=" + contactNumber + ", email=" + email
				+ ", work=" + work + ", address=" + address + ", description=" + description + ", image=" + image
				+ ", currentImage=" + currentImage + "]";
	}



}
