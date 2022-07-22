package com.study.spring.library.dao;

import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class CommentDaoImpl implements CommentDao {

  @PersistenceContext
  private final EntityManager em;

  @Override
  public Collection<Comment> getAll() {
    EntityGraph<?> entityGraph = em.getEntityGraph("comment-graph");
    TypedQuery<Comment> query = em.createQuery("select c from Comment c join c.book", Comment.class);
    query.setHint("javax.persistence.fetchgraph", entityGraph);
    return query.getResultList();
  }

  @Override
  public long create(Comment comment) {
    return save(comment);
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
  public void update(Comment comment) {
    save(comment);
  }

  @Override
  public void deleteById(Long id) {
    //TODO
  }

  private long save(Comment comment) {
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
