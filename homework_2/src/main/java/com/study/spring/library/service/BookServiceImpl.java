package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.dto.CommentedBook;
import com.study.spring.library.exceptions.EntityNotFoundException;
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
    return bookDao.findAll();
  }

  @Override
  @Transactional
  public Book create(Book book, String genre, String author) {
    fillAuthorAndGenre(book, genre, author);
    return bookDao.save(book);
  }

  @Override
  @Transactional(readOnly = true)
  public CommentedBook findById(Long id) {
    Book book = bookDao.findById(id)
                       .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));;
    Hibernate.initialize(book.getComments());
    return new CommentedBook(book);
  }

  @Override
  @Transactional(readOnly = true)
  public CommentedBook findByTitle(String title) {
    Book book = bookDao.findByTitle(title)
                                 .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
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
    book.setGenre(genreDao.findByName(genre).orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre")));
    book.setAuthor(authorDao.findByName(author).orElseThrow(() -> new EntityNotFoundException("Author not found", "Author")));
  }
}
