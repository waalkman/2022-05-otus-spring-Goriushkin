package com.study.spring.library.dao;

import com.study.spring.library.domain.User;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserDao extends MongoRepository<User, String> {

  Optional<User> findByUserName(String userName);

}
