package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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
  public Book create(Book book, String genre, String author) {
    checkAuthorAndGenre(book, genre, author);
    book.setComments(new ArrayList<>());
    return bookDao.save(book);
  }

  @Override
  public Book findById(String id) {
    return bookDao.findById(id)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
  }

  @Override
  public Book findByTitle(String title) {
    return bookDao.findByTitle(title)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
  }

  @Override
  public void update(Book book, String genre, String author) {
    Book currentBook = bookDao.findById(book.getId())
                              .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));

    checkAuthorAndGenre(book, genre, author);
    currentBook.setTitle(book.getTitle());
    currentBook.setDescription(book.getDescription());
    currentBook.setAuthor(book.getAuthor());
    currentBook.setGenre(book.getGenre());

    bookDao.save(currentBook);
  }

  @Override
  public void deleteById(String id) {
    bookDao.deleteById(id);
  }

  private void checkAuthorAndGenre(Book book, String genre, String author) {
    book.setGenre(genreDao.findByName(genre).orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre")));
    book.setAuthor(authorDao.findByName(author).orElseThrow(() -> new EntityNotFoundException("Author not found", "Author")));
  }
}
