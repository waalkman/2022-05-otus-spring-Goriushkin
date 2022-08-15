package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorDao extends JpaRepository<Author, Long> {

  Optional<Author> findByName(String name);

}
