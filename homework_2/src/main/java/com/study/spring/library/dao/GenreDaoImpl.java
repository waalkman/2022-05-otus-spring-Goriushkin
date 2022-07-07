package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
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
public class GenreDaoImpl implements GenreDao {

  private final JdbcOperations jdbc;
  private final NamedParameterJdbcOperations jdbcOperations;

  @Override
  public Collection<Genre> getAll() {
    return jdbc.query("select id, name from genres", new GenreMapper());
  }

  @Override
  public void create(Genre genre) {
    jdbcOperations.update(
        "insert into genres (name) values (:name)",
        Map.of("name", genre.getName()));
  }

  @Override
  public Genre getById(Long id) {
    return jdbcOperations.queryForObject(
        "select id, name from genres where id = :id",
        Map.of("id", id),
        new GenreMapper());
  }

  @Override
  public Long getIdByName(String name) {
    return jdbcOperations.queryForObject(
        "select id from genres where name = :name",
        Map.of("name", name),
        Long.class);
  }

  @Override
  public void update(Genre genre) {
    jdbcOperations.update(
        "update genres set name = :name where id = :id",
        Map.of("id", genre.getId(),
               "name", genre.getName()));
  }

  @Override
  public void deleteById(Long id) {
    jdbcOperations.update("delete from genres where id = :id", Map.of("id", id));
  }

  private static class GenreMapper implements RowMapper<Genre> {

    @Override
    public Genre mapRow(ResultSet rs, int rowNum) throws SQLException {
      return Genre.builder()
                  .id(rs.getLong("id"))
                  .name(rs.getString("name"))
                  .build();
    }
  }
}
