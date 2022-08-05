package com.study.spring.library.dao;

import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentDaoImpl implements CommentDao {

  @PersistenceContext
  private final EntityManager em;

  @Override
  public Collection<Comment> getAll() {
    EntityGraph<?> entityGraph = em.getEntityGraph("comment-graph");
    TypedQuery<Comment> query = em.createQuery("select c from Comment c", Comment.class);
    query.setHint("javax.persistence.fetchgraph", entityGraph);
    return query.getResultList();
  }

  @Override
  public Collection<Comment> getByBookId(Long bookId) {
    TypedQuery<Comment> commentQuery = em.createQuery("select c from Comment c where c.book.id = :bookId", Comment.class);
    commentQuery.setParameter("bookId", bookId);
    return commentQuery.getResultList();
  }

  @Override
  public Comment getById(Long id) {
    Comment comment = em.find(Comment.class, id);
    if (comment == null) {
      throw new EntityNotFoundException("Comment not found", "Comment");
    } else {
      return comment;
    }
  }

  @Override
  public void deleteById(Long id) {
    Comment comment = getById(id);
    em.remove(comment);
  }

  @Override
  public long save(Comment comment) {
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
