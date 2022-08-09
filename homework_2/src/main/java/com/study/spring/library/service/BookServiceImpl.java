package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.dto.CommentedBook;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookDao bookDao;
  private final AuthorDao authorDao;
  private final GenreDao genreDao;

  @Override
  public Collection<Book> getAll() {
    return bookDao.getAll();
  }

  @Override
  @Transactional
  public long create(Book book, String genre, String author) {
    fillAuthorAndGenre(book, genre, author);
    return bookDao.save(book);
  }

  @Override
  @Transactional(readOnly = true)
  public CommentedBook getById(Long id) {
    Book book = bookDao.getById(id);
    Hibernate.initialize(book.getComments());
    return new CommentedBook(book);
  }

  @Override
  @Transactional(readOnly = true)
  public CommentedBook getByTitle(String title) {
    Book book = bookDao.getByTitle(title);
    Hibernate.initialize(book.getComments());
    return new CommentedBook(book);
  }

  @Override
  @Transactional
  public void update(Book book, String genre, String author) {
    fillAuthorAndGenre(book, genre, author);
    bookDao.save(book);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    bookDao.deleteById(id);
  }

  private void fillAuthorAndGenre(Book book, String genre, String author) {
    book.setGenre(genreDao.getByName(genre));
    book.setAuthor(authorDao.getByName(author));
  }
}
