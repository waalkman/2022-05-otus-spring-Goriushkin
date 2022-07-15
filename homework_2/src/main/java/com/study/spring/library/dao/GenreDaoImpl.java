package com.study.spring.library.dao;

import com.study.spring.library.domain.Genre;
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
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

  private final JdbcOperations jdbc;
  private final NamedParameterJdbcOperations jdbcOperations;

  @Override
  public Collection<Genre> getAll() {
    try {
      return jdbc.query("select id, name from genres", new GenreMapper());
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot get all genres", e);
    }
  }

  @Override
  public long create(Genre genre) {
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      SqlParameterSource sqlParameterSource = new MapSqlParameterSource(Map.of("name", genre.getName()));
      jdbcOperations.update(
          "insert into genres (name) values (:name)",
          sqlParameterSource,
          keyHolder,
          new String[] { "id" });

      return keyHolder.getKey() == null ? -1L : keyHolder.getKey().longValue();
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot create genre", e);
    }
  }

  @Override
  public Genre getById(Long id) {
    return findById(id);
  }

  @Override
  public Long getIdByName(String name) {
    try {
      return jdbcOperations.queryForObject(
          "select id from genres where name = :name",
          Map.of("name", name),
          Long.class);
    } catch (IncorrectResultSizeDataAccessException e) {
      throw new EntityNotFoundException("Genre not found", e, "Genre");
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot get genre by name", e);
    }
  }

  @Override
  public void update(Genre genre) {
    findById(genre.getId());
    try {
      jdbcOperations.update(
          "update genres set name = :name where id = :id",
          Map.of("id", genre.getId(), "name", genre.getName()));
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot update genre", e);
    }
  }

  @Override
  public void deleteById(Long id) {
    try {
      jdbcOperations.update("delete from genres where id = :id", Map.of("id", id));
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot delete genre by id", e);
    }
  }

  private Genre findById(Long id) {
    try {
      return jdbcOperations.queryForObject(
          "select id, name from genres where id = :id",
          Map.of("id", id),
          new GenreMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      throw new EntityNotFoundException("Genre not found", e, "Genre");
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot get genre by id", e);
    }
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
