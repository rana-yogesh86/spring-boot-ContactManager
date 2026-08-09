package com.contact.controllers;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.contact.entity.User;
import com.contact.entity.UserRole;
import com.contact.services.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {

	@Autowired
	private AdminService adminService;

	@GetMapping("/dashboard")
	public String showUsers(
			@RequestParam(defaultValue = "" , required = false) String role,
			@RequestParam(defaultValue = "" , required = false) String status,
			@RequestParam(defaultValue = "0") int page , Model model) {

		Pageable pageable = PageRequest.of(page, 5 , Sort.by("registeredOn").descending());

		Page<User> allUsers = this.adminService.allUsers(UserRole.USER, pageable);
		model.addAttribute("users", allUsers);
		model.addAttribute("totalUsers", this.adminService.countByRole(UserRole.USER));
		model.addAttribute("totalAdmins", this.adminService.countByRole(UserRole.ADMIN));
		model.addAttribute("totalActive", this.adminService.countByActive(true));
		model.addAttribute("totalSuspended", this.adminService.countByActive(false));

		return "admin/admin-dashboard";
	}

}
