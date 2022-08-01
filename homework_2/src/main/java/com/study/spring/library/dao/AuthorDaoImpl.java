package com.study.spring.library.dao;

import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class AuthorDaoImpl implements AuthorDao {

  @PersistenceContext
  private final EntityManager em;

  @Override
  @Transactional(readOnly = true)
  public Collection<Author> getAll() {
    TypedQuery<Author> authorQuery = em.createQuery("select a from Author a", Author.class);
    return authorQuery.getResultList();
  }

  @Override
  @Transactional
  public long create(Author author) {
    return save(author);
  }

  @Override
  @Transactional(readOnly = true)
  public Author getById(Long id) {
    Author author = em.find(Author.class, id);
    if (author == null) {
      throw new EntityNotFoundException("Author not found", "Author");
    } else {
      return author;
    }
  }

  @Override
  @Transactional(readOnly = true)
  public Author getByName(String name) {
    TypedQuery<Author> authorQuery = em.createQuery("select a from Author a where a.name = :name", Author.class);
    authorQuery.setParameter("name", name);

    List<Author> foundAuthors = authorQuery.getResultList();
    if (foundAuthors.size() == 1) {
      return foundAuthors.iterator().next();
    } else {
      throw new EntityNotFoundException("Author not found", "Author");
    }
  }

  @Override
  @Transactional
  public void update(Author author) {
    save(author);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Author author = getById(id);
    em.remove(author);
  }

  private long save(Author author) {
    if (author.getId() == null) {
      em.persist(author);
    } else if (em.find(Author.class, author.getId()) != null) {
      em.merge(author);
    } else {
      throw new EntityNotFoundException("Author not found", "Author");
    }
    return author.getId();
  }

}
