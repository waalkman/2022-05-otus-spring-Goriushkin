package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
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
    return jdbc.query("select id, name from authors", new AuthorMapper());
  }

  @Override
  public void create(Author author) {
    jdbcOperations.update(
        "insert into authors (name) values (:name)",
        Map.of("name", author.getName()));
  }

  @Override
  public Author getById(Long id) {
    return jdbcOperations.queryForObject(
        "select id, name from authors where id = :id",
        Map.of("id", id),
        new AuthorMapper());
  }

  @Override
  public Long getIdByName(String name) {
    return jdbcOperations.queryForObject(
        "select id from authors where name = :name",
        Map.of("name", name),
        Long.class);
  }

  @Override
  public void update(Author author) {
    jdbcOperations.update(
        "update authors set name = :name where id = :id",
        Map.of("id", author.getId(),
               "name", author.getName()));
  }

  @Override
  public void deleteById(Long id) {
    jdbcOperations.update("delete from authors where id = :id", Map.of("id", id));
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
