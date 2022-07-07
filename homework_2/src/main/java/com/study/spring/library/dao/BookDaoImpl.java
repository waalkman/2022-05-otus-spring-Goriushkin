package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

  private final JdbcOperations jdbc;
  private final NamedParameterJdbcOperations jdbcOperations;

  @Override
  public Collection<Book> getAll() {
    return jdbc.query("select id, title, description, genre_id, author_id from books", new BookMapper());
  }

  @Override
  public void create(Book book) {
    jdbcOperations.update(
        "insert into books (title, description, genre_id, author_id) values (:title, :description, :genreId, :authorId)",
        Map.of(
            "title", book.getTitle(),
            "description", book.getDescription(),
            "genreId", book.getGenreId(),
            "authorId", book.getAuthorId()));
  }

  @Override
  public Book getById(Long id) {
    return jdbcOperations.queryForObject(
        "select id, title, description, genre_id, author_id from books where id = :id",
        Map.of("id", id),
        new BookMapper());
  }

  @Override
  public void update(Book book) {
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
    jdbcOperations.update("delete from books where id = :id", Map.of("id", id));
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
