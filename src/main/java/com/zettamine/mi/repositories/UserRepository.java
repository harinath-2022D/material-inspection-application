package com.zettamine.mi.repositories;

import java.io.Serializable;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.zettamine.mi.entities.User;
@Repository
public interface UserRepository extends JpaRepository<User, Serializable> {

	Optional<User> findByUsername(String username);

}
