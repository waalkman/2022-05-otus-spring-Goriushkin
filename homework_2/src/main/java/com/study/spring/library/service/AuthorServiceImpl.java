package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.ConsistencyException;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {

  private final AuthorDao authorDao;
  private final BookDao bookDao;
  private final MutableAclService mutableAclService;

  @Override
  public Collection<Author> getAll() {
    return authorDao.findAll();
  }

  @Override
  public Author create(Author author) {
    return save(author);
  }

  @Override
  public Author findById(String id) {
    return authorDao.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found", "Author"));
  }

  @Override
  public Author findByName(String name) {
    return authorDao.findByName(name)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found", "Author"));
  }

  @Override
  public void update(Author author) {
    save(author);
  }

  private Author save(Author author) {
    if (authorDao.existsByName(author.getName())) {
      throw new ConsistencyException("Cannot create author. There is another author with that name in database!");
    } else {
      return authorDao.save(author);
    }
  }

  @Override
  public void deleteById(String id) {
    if (bookDao.existsByAuthorId(id)) {
      throw new ConsistencyException("Cannot delete author. There are book(s) with that author in database!");
    } else {
      authorDao.deleteById(id);
    }
  }
}
