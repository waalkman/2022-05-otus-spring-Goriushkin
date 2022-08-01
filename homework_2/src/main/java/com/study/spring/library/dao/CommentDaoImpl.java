package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class CommentDaoImpl implements CommentDao {

  @PersistenceContext
  private final EntityManager em;
  private final BookDao bookDao;

  @Override
  @Transactional(readOnly = true)
  public Collection<Comment> getAll() {
    EntityGraph<?> entityGraph = em.getEntityGraph("comment-graph");
    TypedQuery<Comment> query = em.createQuery("select c from Comment c", Comment.class);
    query.setHint("javax.persistence.fetchgraph", entityGraph);
    return query.getResultList();
  }

  @Override
  @Transactional
  public long create(Comment comment, String bookTitle) {
    return save(comment, bookTitle);
  }

  @Override
  @Transactional(readOnly = true)
  public Comment getById(Long id) {
    Comment comment = em.find(Comment.class, id);
    if (comment == null) {
      throw new EntityNotFoundException("Comment not found", "Comment");
    } else {
      return comment;
    }
  }

  @Override
  @Transactional
  public void update(Comment comment, String bookTitle) {
    save(comment, bookTitle);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    Comment comment = getById(id);
    em.remove(comment);
  }

  private long save(Comment comment, String bookTitle) {
    Book book = bookDao.getByTitle(bookTitle);
    comment.setBook(book);

    if (comment.getId() == null) {
      em.persist(comment);
    } else if (em.find(Comment.class, comment.getId()) != null) {
      em.merge(comment);
    } else {
      throw new EntityNotFoundException("Comment not found", "Comment");
    }
    return comment.getId();
  }
}
