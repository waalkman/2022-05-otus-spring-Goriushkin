package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GenreDao extends JpaRepository<Genre, Long> {

  Optional<Genre> findByName(String name);

}
