package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.DataQueryException;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

  private final JdbcOperations jdbc;
  private final NamedParameterJdbcOperations jdbcOperations;

  @Override
  public Collection<Author> getAll() {
    try {
      return jdbc.query("select id, name from authors", new AuthorMapper());
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot get all authors", e);
    }
  }

  @Override
  public void create(Author author) {
    try {
      jdbcOperations.update(
          "insert into authors (name) values (:name)",
          Map.of("name", author.getName()));
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot create author", e);
    }
  }

  @Override
  public Author getById(Long id) {
    return findById(id);
  }

  @Override
  public Long getIdByName(String name) {
    try {
      return jdbcOperations.queryForObject(
          "select id from authors where name = :name",
          Map.of("name", name),
          Long.class);
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot get author by name", e);
    }
  }

  @Override
  public void update(Author author) {
    findById(author.getId());
    try {
      jdbcOperations.update(
          "update authors set name = :name where id = :id",
          Map.of("id", author.getId(),
                 "name", author.getName()));
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot update author", e);
    }
  }

  @Override
  public void deleteById(Long id) {
    try {
      jdbcOperations.update("delete from authors where id = :id", Map.of("id", id));
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot delete author", e);
    }
  }

  private Author findById(Long id) {
    try {
      return jdbcOperations.queryForObject(
          "select id, name from authors where id = :id",
          Map.of("id", id),
          new AuthorMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      throw new EntityNotFoundException("Author not found", e);
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot get author by id", e);
    }
  }

  private static class AuthorMapper implements RowMapper<Author> {

    @Override
    public Author mapRow(ResultSet rs, int rowNum) throws SQLException {
      return Author.builder()
                   .id(rs.getLong("id"))
                   .name(rs.getString("name"))
                   .build();
    }
  }
}
