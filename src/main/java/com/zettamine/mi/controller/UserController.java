package com.zettamine.mi.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.zettamine.mi.entities.User;
import com.zettamine.mi.requestdtos.LoginDto;
import com.zettamine.mi.services.UserService;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/")
public class UserController {

	private UserService userService;

	public UserController(UserService userService) {
		super();
		this.userService = userService;
	}

	@GetMapping(path = { "", "/" })
	public String getLoginPage(Model model) {

		model.addAttribute("user", new LoginDto());
		
		return "index";
	}

	@PostMapping("/login")
	public String validateUser(Model model, LoginDto user,HttpSession session, RedirectAttributes redirectAttributes) {
		System.out.println(user);
		boolean isAuthenticated = userService.validateUser(user, session);

		if (isAuthenticated) {
			
			
			return "home";

		} else {
			redirectAttributes.addFlashAttribute("error", "Invalid username or password");
			return "redirect:/";
		}
	}

	@GetMapping("/add/user")
	public void addUser(@RequestParam("username") String name, @RequestParam("password") String pass) {
		userService.addUser(new User(name, pass));
	}
}
