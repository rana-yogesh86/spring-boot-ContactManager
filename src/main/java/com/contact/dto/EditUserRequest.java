package com.contact.dto;

import java.time.LocalDateTime;

import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class EditUserRequest {

	@NotBlank
	@Size(min=5,max=20,message = "must be between 5-20 characters")
	private String name;

	@Email
	@NotBlank
	private String email;

	private LocalDateTime registeredOn;

	private String newPassword;

	private String confirmPassword;

	private String currentPassword;

	private String currentImage;

	private MultipartFile newImage;

	@Size(min = 10,max = 10 , message = "invalid phone no.")
	private String phone;

	private String bio;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDateTime getRegisteredOn() {
		return registeredOn;
	}
	public void setRegisteredOn(LocalDateTime registeredOn) {
		this.registeredOn = registeredOn;
	}
	public String getNewPassword() {
		return newPassword;
	}
	public void setNewPassword(String newPassword) {
		this.newPassword = newPassword;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public MultipartFile getNewImage() {
		return newImage;
	}
	public void setImage(MultipartFile newImage) {
		this.newImage = newImage;
	}
	public String getCurrentImage() {
		return currentImage;
	}
	public void setCurrentImage(String currentImage) {
		this.currentImage = currentImage;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getBio() {
		return bio;
	}
	public void setBio(String bio) {
		this.bio = bio;
	}
	public void setNewImage(MultipartFile newImage) {
		this.newImage = newImage;
	}
	public String getCurrentPassword() {
		return currentPassword;
	}
	public void setCurrentPassword(String currentPassword) {
		this.currentPassword = currentPassword;
	}
	@Override
	public String toString() {
		return "EditUserRequest [name=" + name + ", email=" + email + ", newPassword=" + newPassword
				+ ", confirmPassword=" + confirmPassword + ", currentPassword=" + currentPassword + ", currentImage="
				+ currentImage + ", newImage=" + newImage + ", phone=" + phone + ", bio=" + bio + "]";
	}


}
