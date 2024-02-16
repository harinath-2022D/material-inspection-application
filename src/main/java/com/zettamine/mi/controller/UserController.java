package com.zettamine.mi.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zettamine.mi.entities.User;
import com.zettamine.mi.requestdtos.LoginDto;
import com.zettamine.mi.services.UserService;
import com.zettamine.mi.utils.AppConstants;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class UserController {

	private UserService userService;
	
	private Logger LOG = LoggerFactory.getLogger(UserController.class);

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping(path = { "", "/" })
	public String getLoginPage(Model model) {

		model.addAttribute("user", new LoginDto());
		
		return AppConstants.INDEX;
	}

	@PostMapping("/login")
	public String validateUser(Model model, LoginDto user,HttpSession session, RedirectAttributes redirectAttributes) {
	
		boolean isAuthenticated = userService.validateUser(user, session);

		if (isAuthenticated) {
			
			LOG.info("User verification is success | {}", user);
			
			return AppConstants.HOME_PAGE;

		} else {
			
			LOG.warn("INvalid user attempting to login | {}", user);
			
			redirectAttributes.addFlashAttribute("error", "Invalid username or password");
			
			return "redirect:/";
		}
	}

	@GetMapping("/add/user")
	public void addUser(@RequestParam("username") String name, @RequestParam("password") String pass) {
		
		LOG.info("new User registerd");
		userService.addUser(new User(name, pass));
	}
	
	@GetMapping("/home")
	public String showHomePage() {
		
		LOG.info("returning home page");
		
		return AppConstants.HOME_PAGE;
	}
}
