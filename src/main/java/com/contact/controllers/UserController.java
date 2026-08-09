package com.contact.controllers;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import com.contact.dto.EditUserRequest;
import com.contact.entity.Contact;
import com.contact.entity.User;
import com.contact.services.ContactService;
import com.contact.services.UserService;
import com.security.CustomUserDetails;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private ContactService contactService;

	@Autowired
	private BCryptPasswordEncoder passEncoder;

	List<String> allowedFileTypes = List.of(
			"image/jpeg",
			"image/png",
			"image/webp",
			"application/octet-stream");


	@GetMapping("/dashboard")
	public String showDashboard(@AuthenticationPrincipal CustomUserDetails userDetails , Model model) {

		User user = userDetails.getUser();

		int totalContacts = this.contactService.contactsByUser(user).size();
		List<Contact> addedThisWeek = this.contactService.
				contactsOfUserAddedInWeek(user.getId() , LocalDateTime.now().minusDays(7));

		model.addAttribute("recentContacts", addedThisWeek);
		model.addAttribute("addedThisWeek", addedThisWeek.size());
		model.addAttribute("totalContacts" , totalContacts);


		return "user/dashboard";
	}

	@GetMapping("/edit")
	public String editProfile(@AuthenticationPrincipal CustomUserDetails userDetails,Model model) {

		User existingUser = userDetails.getUser();

		EditUserRequest editUser = new EditUserRequest();
		editUser.setEmail(existingUser.getEmail());
		editUser.setName(existingUser.getName());
		editUser.setRegisteredOn(existingUser.getRegisteredOn());
		editUser.setCurrentImage(existingUser.getImage());
		editUser.setCurrentPassword(existingUser.getPassword());

		model.addAttribute("profileForm", editUser);
		System.out.println(editUser.toString());

		return "user/profile-edit";
	}

	@PostMapping("/update")
	public String updateProfile(@Valid @ModelAttribute("profileForm") EditUserRequest editUserRequest ,
			BindingResult bindingResult, Model model) throws IOException {

		MultipartFile newImage = editUserRequest.getNewImage();

		System.out.println(editUserRequest.toString());

		User updateUser = this.userService.findByEmail(editUserRequest.getEmail());

		if (bindingResult.hasErrors()) {
			bindingResult.getAllErrors().forEach(e -> System.out.println(e.toString()));
			return "user/profile-edit";
		}

		String regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&_])[A-Za-z\\d@$!%*?&_]{8,}$";

		if(!editUserRequest.getNewPassword().isBlank()) {

			if(passEncoder.matches(editUserRequest.getNewPassword(), editUserRequest.getCurrentPassword())){
				bindingResult.rejectValue("newPassword", "invalid.password",
						"new password can't be same as old password");
			}

			if(!editUserRequest.getNewPassword().equals(editUserRequest.getConfirmPassword())) {
				bindingResult.rejectValue("confirmPassword", "password.mismatch", "passwords do not match");
			}

			if(!editUserRequest.getNewPassword().matches(regexp)) {
				bindingResult.rejectValue("newPassword", "invalid.password",
						"must be atleast 8 characters ,should have at least one lowercase letter, "
								+"one uppercase letter, one number, one special character");
			}

		}
		else {
//			System.out.println("current password : "+editUserRequest.getCurrentPassword());
//			String currentPassword = editUserRequest.getCurrentPassword();
//			editUserRequest.setNewPassword(currentPassword);
		}


		Path updatedImage = null;

		if(!newImage.isEmpty()) {

			if(!allowedFileTypes.contains(newImage.getContentType())) {
				bindingResult.rejectValue("newImage", "invalid.image",
						"upload only ,image/jpeg | image/png | image/webp format");
			}

			String imgName = UUID.randomUUID()+"_"+newImage.getOriginalFilename();
			updatedImage = Path.of("uploads", "user_profile_images" ,imgName );

			//create directories if not existed...
			Files.createDirectories(updatedImage.getParent());
			Files.copy(newImage.getInputStream(), updatedImage, StandardCopyOption.REPLACE_EXISTING);
		} else {
			updatedImage = Path.of(editUserRequest.getCurrentImage());
		}



		if(!bindingResult.hasErrors()) {

			System.out.println(editUserRequest.toString());

			updateUser.setName(editUserRequest.getName());
			if(!editUserRequest.getNewPassword().isBlank()) {
				updateUser.setPassword(passEncoder.encode(editUserRequest.getNewPassword()));
			}
			updateUser.setImage(updatedImage.toString());
			updateUser.setPhone(editUserRequest.getPhone());
			updateUser.setBio(editUserRequest.getBio());
			this.userService.saveUser(updateUser);

			model.addAttribute("successMessage", "profile updated successfully!");
		}
		else {
			model.addAttribute("errorMessage", "Something went wrong!");
		}
		return "user/profile-edit";
	}

	@GetMapping("/profile")
	public String userProfile(@AuthenticationPrincipal CustomUserDetails userDetails, Model model) {
		model.addAttribute("user", userDetails.getUser());
		return "user/profile";
	}
}
