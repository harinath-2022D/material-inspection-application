package com.zettamine.mi.services;

import com.zettamine.mi.entities.User;
import com.zettamine.mi.requestdtos.LoginDto;

import jakarta.servlet.http.HttpSession;

public interface UserService {
	
	boolean validateUser(LoginDto user, HttpSession session);

	void addUser(User user);
}
