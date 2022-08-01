package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class BookDaoImpl implements BookDao {

  @PersistenceContext
  private final EntityManager em;
  private final GenreDao genreDao;
  private final AuthorDao authorDao;

  @Override
  @Transactional(readOnly = true)
  public Collection<Book> getAll() {
    EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
    TypedQuery<Book> query = em.createQuery("select b from Book b", Book.class);
    query.setHint("javax.persistence.fetchgraph", entityGraph);
    return query.getResultList();
  }

  @Override
  @Transactional
  public long create(Book book, String genre, String author) {
    return save(book, genre, author);
  }

  @Override
  @Transactional(readOnly = true)
  public Book getById(Long id) {
    Book book = em.find(Book.class, id);
    if (book == null) {
      throw new EntityNotFoundException("Book not found", "Book");
    } else {
      return book;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Book getByTitle(String title) {
    EntityGraph<?> entityGraph = em.getEntityGraph("book-entity-graph");
    TypedQuery<Book> bookQuery = em.createQuery("select b from Book b where b.title = :title", Book.class);
    bookQuery.setParameter("title", title);
    bookQuery.setHint("javax.persistence.fetchgraph", entityGraph);

    List<Book> books = bookQuery.getResultList();
    if (books.size() == 1) {
      return books.iterator().next();
    } else {
      throw new EntityNotFoundException("Book not found", "Book");
    }
  }

  @Override
  @Transactional
  public void update(Book book, String genre, String author) {
    save(book, genre, author);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Book book = getById(id);
    em.remove(book);
  }

  private long save(Book book, String genre, String author) {
    book.setGenre(genreDao.getByName(genre));
    book.setAuthor(authorDao.getByName(author));

    if (book.getId() == null) {
      em.persist(book);
    } else if (em.find(Book.class, book.getId()) != null) {
      em.merge(book);
    } else {
      throw new EntityNotFoundException("Book not found", "Book");
    }
    return book.getId();
  }
}
