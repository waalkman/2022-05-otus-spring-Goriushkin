package com.study.spring.batch.postgre.dao;

import com.study.spring.batch.postgre.domain.Author;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorDaoPG extends JpaRepository<Author, Long> {

  Optional<Author> findByMongoId(String mongoId);

}
