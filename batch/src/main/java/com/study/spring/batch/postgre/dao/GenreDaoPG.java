package com.study.spring.batch.postgre.dao;

import com.study.spring.batch.postgre.domain.Genre;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreDaoPG extends JpaRepository<Genre, Long> {

  Optional<Genre> findByMongoId(String mongoId);

}