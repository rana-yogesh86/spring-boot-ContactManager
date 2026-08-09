package com.contact.exceptions;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;


@ControllerAdvice
public class MvcExceptionHandler  {


	@ExceptionHandler(NoContactsFoundException.class)
	public String handleContactsNotFound(NoContactsFoundException e , Model model) {

		model.addAttribute("err_msg" , e.getMessage());
		model.addAttribute("status" , HttpStatus.NOT_FOUND);
		return "error/contacts-not-found";
	}

	@ExceptionHandler(UserNotFoundException.class)
	public String handleUserNotFound(UserNotFoundException e , Model model) {

		model.addAttribute("err_msg" , e.getMessage());
		model.addAttribute("status" , HttpStatus.NOT_FOUND);
		return "error/user-not-found";
	}

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public String handleEmailAlreadyExists(EmailAlreadyExistsException e , Model model) {

		model.addAttribute("errorMessage" , e.getMessage());
		model.addAttribute("status" , HttpStatus.NOT_FOUND);
		return "/public/register";

	}

	@ExceptionHandler(DataIntegrityViolationException.class)
	public String handleDataIntegrityViolation(DataIntegrityViolationException e ,
			HttpServletRequest request, RedirectAttributes rAttributes) {

		rAttributes.addFlashAttribute("errorMessage", "ERROR : "+HttpStatus.CONFLICT
				+" : Record already exists,please check details");
		return "redirect:"+request.getHeader("Referer");
	}


	@ExceptionHandler(MaxUploadSizeExceededException.class)
	public String handleFileSizeViolation(MaxUploadSizeExceededException e , HttpServletRequest request ,
			RedirectAttributes redirectAttributes) {
		redirectAttributes.addFlashAttribute("errorMessage", "file size exceeds above 2MB");

		return "redirect:"+request.getHeader("Referer");
	}



//	@ExceptionHandler(Exception.class)
//	public String handleOther(Exception e , HttpServletRequest request ,RedirectAttributes redirectAttributes) {
//
//		String error_msg = "ERROR : "+HttpStatus.INTERNAL_SERVER_ERROR+
//				" : Something , went wrong , please try again";
//		redirectAttributes.addFlashAttribute("errorMessage", error_msg);
//
//		return "redirect:"+request.getHeader("Referer");
//	}

}
