package com.contact.controllers;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.contact.dto.RegisterRequest;
import com.contact.entity.User;
import com.contact.services.UserService;

import jakarta.validation.Valid;

@Controller
public class HomeController {

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String home(Model model) {

		model.addAttribute("activePage" , "home");
		return "public/index";
	}

	@GetMapping("/about")
	public String about(Model model) {

		model.addAttribute("activePage" , "about");
		return "public/about";
	}

	@GetMapping("/contact-us")
	public String contactUs(Model model) {

		model.addAttribute("activePage" , "contactUs");
		return "public/contact";
	}

	@GetMapping("/help")
	public String help(Model model) {

		model.addAttribute("activePage" , "help");
		return "public/help";
	}

	@GetMapping("/login")
	public String login(Model model) {
		return "public/login";
	}



	@GetMapping("/sign-up")
	public String signUp(Model model) {
		model.addAttribute("registerRequest" , new RegisterRequest());
		return "public/register";
	}

	@PostMapping("/process-signup")
	public String processSignUp(@Valid @ModelAttribute RegisterRequest registerRequest ,
			BindingResult bindingResult , Model model) {

		if(this.userService.existsByEmail(registerRequest.getEmail())) {
			model.addAttribute("errorMessage" , "email already exists , login instead");
			return "public/register";
		}

		if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
			bindingResult.rejectValue(
					"confirmPassword", "password.mismatch","password do not match!");
			return "public/register";
		}


		User newUser = new User();
		newUser.setName(registerRequest.getFullName());
		newUser.setEmail(registerRequest.getEmail().toLowerCase());
		newUser.setPassword(passwordEncoder.encode(registerRequest.getPassword()) );
		newUser.setImage("uploads/default_user.png");

		this.userService.addNewUser(newUser);

		model.addAttribute("newuser", registerRequest.getFullName());
		return "public/signup-success";
	}


}
