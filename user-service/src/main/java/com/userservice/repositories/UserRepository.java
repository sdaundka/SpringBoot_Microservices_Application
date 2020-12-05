package com.userservice.repositories;

import org.springframework.data.repository.CrudRepository;
import com.userservice.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {

	User findByNameAndEmail(String name, String email);

}
