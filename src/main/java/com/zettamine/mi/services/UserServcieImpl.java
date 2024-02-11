package com.zettamine.mi.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zettamine.mi.entities.User;
import com.zettamine.mi.repositories.UserRepository;
import com.zettamine.mi.requestdtos.LoginDto;

import jakarta.servlet.http.HttpSession;

@Service
public class UserServcieImpl implements UserService {
	private UserRepository userRepo;
	

	public UserServcieImpl(UserRepository userRepo) {
		super();
		this.userRepo = userRepo;
	}

	@Override
	public boolean validateUser(LoginDto user, HttpSession session) {
		Optional<User> optUser = userRepo.findByUsername(user.getUsername());
//		long cnt = userRepo.count();

		if (optUser.isEmpty()) {
			return false;
		}

		else {
			User currUser = optUser.get();
			if (currUser.getPassword().equals(user.getPassword())) {
				session.setAttribute("userId", currUser.getUserId());
				return true;
			}
		}
		return false;
	}

	@Override
	public void addUser(User user) {
		User savedUser = userRepo.save(user);
		System.out.println("user saved" + savedUser.getUserId());

	}
}
