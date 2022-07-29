package com.study.spring.library.dao;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import javax.persistence.PersistenceException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import(CommentDaoImpl.class)
class CommentDaoImplTest {

  @Autowired
  private CommentDaoImpl commentDao;
  @Autowired
  private TestEntityManager em;

  @Test
  void getAll_success() {
    int expectedCommentsSize = 2;
    int actualCommentsSize = commentDao.getAll().size();
    assertEquals(expectedCommentsSize, actualCommentsSize);
  }

  @Test
  void create_success() {
    Book book = DaoTestUtils.createBook(em, "Book title", "Book description");
    Comment comment = Comment.builder()
                             .text("Comment text")
                             .userName("Comment author")
                             .book(book)
                             .build();

    long commentId = commentDao.create(comment);

    Comment commentFromDb = em.find(Comment.class, commentId);
    assertEquals(comment, commentFromDb);
  }

  @Test
  void create_tooLongUsername_success() {
    Book book = DaoTestUtils.createBook(em, "Book title", "Book description");
    Comment comment = Comment.builder()
                             .text("Comment text")
                             .userName("Comment author that is veeeeeery looooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong")
                             .book(book)
                             .build();

    assertThrows(PersistenceException.class, () -> commentDao.create(comment));
  }

  @Test
  void getById_success() {
    Comment expectedComment = DaoTestUtils.createComment(em);

    Comment actualComment = commentDao.getById(expectedComment.getId());

    assertEquals(expectedComment, actualComment);
  }

  @Test
  void getById_notExistingComment_success() {
    assertThrows(EntityNotFoundException.class, () -> commentDao.getById(Long.MAX_VALUE));
  }

  @Test
  void update_success() {
    String text = "updated text";
    String userName = "userName";
    Comment createdComment = DaoTestUtils.createComment(em);
    em.detach(createdComment);

    createdComment.setText(text);
    createdComment.setUserName(userName);

    commentDao.update(createdComment);

    Comment updatedComment = em.find(Comment.class, createdComment.getId());

    assertEquals(text, updatedComment.getText());
    assertEquals(userName, updatedComment.getUserName());
  }

  @Test
  void deleteById_success() {
    Comment createdComment = DaoTestUtils.createComment(em);
    em.detach(createdComment);
    long commentId = createdComment.getId();

    commentDao.deleteById(commentId);

    assertNull(em.find(Comment.class, commentId));
  }

}