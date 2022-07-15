package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
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
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

  private final JdbcOperations jdbc;
  private final NamedParameterJdbcOperations jdbcOperations;

  @Override
  public Collection<Book> getAll() {
    try {
      return jdbc.query("select id, title, description, genre_id, author_id from books", new BookMapper());
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot get all books", e);
    }
  }

  @Override
  public long create(Book book) {
    try {
      KeyHolder keyHolder = new GeneratedKeyHolder();
      SqlParameterSource sqlParameterSource =
          new MapSqlParameterSource(
              Map.of(
                  "title", book.getTitle(),
                  "description", book.getDescription(),
                  "genreId", book.getGenreId(),
                  "authorId", book.getAuthorId()));

      jdbcOperations.update(
          "insert into books (title, description, genre_id, author_id) values (:title, :description, :genreId, :authorId)",
          sqlParameterSource,
          keyHolder,
          new String[] { "id" });

      return keyHolder.getKey() == null ? -1L : keyHolder.getKey().longValue();
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot create book", e);
    }
  }

  @Override
  public Book getById(Long id) {
    return findById(id);
  }

  @Override
  public void update(Book book) {
    findById(book.getId());
    jdbcOperations.update(
        "update books set title = :title, description = :description, genre_id = :genreId, author_id = :authorId where id = :id",
        Map.of("id", book.getId(),
               "title", book.getTitle(),
               "description", book.getDescription(),
               "genreId", book.getGenreId(),
               "authorId", book.getAuthorId()));
  }

  @Override
  public void deleteById(Long id) {
    try {
      jdbcOperations.update("delete from books where id = :id", Map.of("id", id));
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot delete book", e);
    }
  }

  private Book findById(Long id) {
    try {
      return jdbcOperations.queryForObject(
          "select id, title, description, genre_id, author_id from books where id = :id",
          Map.of("id", id),
          new BookMapper());
    } catch (IncorrectResultSizeDataAccessException e) {
      throw new EntityNotFoundException("Book not found", e, "Book");
    } catch (DataAccessException e) {
      throw new DataQueryException("Cannot get book by id", e);
    }
  }

  private static class BookMapper implements RowMapper<Book> {

    @Override
    public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
      return Book.builder()
                 .id(rs.getLong("id"))
                 .title(rs.getString("title"))
                 .description(rs.getString("description"))
                 .genreId(rs.getLong("genre_id"))
                 .authorId(rs.getLong("author_id"))
                 .build();
    }
  }
}
