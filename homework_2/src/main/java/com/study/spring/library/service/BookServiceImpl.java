package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookDao bookDao;
  private final AuthorDao authorDao;
  private final GenreDao genreDao;

  @Override
  @Transactional(readOnly = true)
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
  public Book getById(Long id) {
    return bookDao.getById(id);
  }

  @Override
  @Transactional(readOnly = true)
  public Book getByTitle(String title) {
    return bookDao.getByTitle(title);
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
