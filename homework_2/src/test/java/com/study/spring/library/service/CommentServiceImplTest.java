package com.study.spring.library.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CommentServiceImplTest {

  @Mock
  private BookDao bookDao;
  @InjectMocks
  private CommentServiceImpl commentService;

  @Test
  void create_success() {
    String bookTitle = "title";
    Comment comment = Comment.builder().text("text").userName("user").id("1232").build();
    Book expectedSaveBook = Book.builder().comments(List.of(comment)).build();

    when(bookDao.findByTitle(bookTitle)).thenReturn(Optional.of(Book.builder().comments(new ArrayList<>()).build()));

    commentService.create(comment, bookTitle);

    verify(bookDao).findByTitle(bookTitle);
    verify(bookDao).save(expectedSaveBook);
  }

  @Test
  void create_bookNotFound() {
    String bookTitle = "title";

    when(bookDao.findByTitle(bookTitle)).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> commentService.create(null, bookTitle));
  }

  @Test
  void findById_success() {
    String bookTitle = "title";
    String desiredCommentId = "76745";
    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
    Book book = Book.builder()
                    .title(bookTitle)
                    .comments(List.of(commentOne, commentTwo))
                    .build();

    when(bookDao.findByTitle(bookTitle)).thenReturn(Optional.of(book));

    Comment actualComment = commentService.findById(bookTitle, desiredCommentId);

    assertEquals(commentTwo, actualComment);
  }

  @Test
  void findById_bookNotFound() {
    when(bookDao.findByTitle(any())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> commentService.findById(null, null));
  }

  @Test
  void findById_commentNotFound() {
    String bookTitle = "title";
    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
    Book book = Book.builder()
                    .title(bookTitle)
                    .comments(List.of(commentOne))
                    .build();

    when(bookDao.findByTitle(bookTitle)).thenReturn(Optional.of(book));

    assertThrows(EntityNotFoundException.class, () -> commentService.findById(bookTitle, "nu11"));
  }

  @Test
  void findByBookTitle_success() {
    String bookTitle = "title";
    String desiredCommentId = "76745";
    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
    List<Comment> expectedBookComments = List.of(commentOne, commentTwo);
    Book book = Book.builder()
                    .title(bookTitle)
                    .comments(expectedBookComments)
                    .build();

    when(bookDao.findByTitle(bookTitle)).thenReturn(Optional.of(book));

    Collection<Comment> actualComments = commentService.findByBookTitle(bookTitle);

    assertThat(actualComments).containsExactlyElementsOf(expectedBookComments);
  }

  @Test
  void findByBookTitle_bookNotFound() {
    when(bookDao.findByTitle(any())).thenReturn(Optional.empty());

    assertThrows(EntityNotFoundException.class, () -> commentService.findByBookTitle("nu11"));
  }

  @Test
  void update_success() {
    String bookTitle = "title";
    String desiredCommentId = "76745";
    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
    Comment updatedComment = Comment.builder().text("new_label").userName("god").id(desiredCommentId).build();
    Book book = Book.builder()
                    .title(bookTitle)
                    .comments(List.of(commentOne, commentTwo))
                    .build();

    when(bookDao.findByTitle(bookTitle)).thenReturn(Optional.of(book));

    commentService.update(updatedComment, bookTitle);

    assertThat(book.getComments()).contains(updatedComment);
    assertThat(book.getComments()).doesNotContain(commentTwo);
  }

  @Test
  void update_commentNotFound() {
    String bookTitle = "title";
    String desiredCommentId = "76745";
    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
    Comment updatedComment = Comment.builder().text("new_label").userName("god").id("where is it?").build();
    Book book = Book.builder()
                    .title(bookTitle)
                    .comments(List.of(commentOne, commentTwo))
                    .build();

    when(bookDao.findByTitle(bookTitle)).thenReturn(Optional.of(book));

    assertThrows(EntityNotFoundException.class, () -> commentService.update(updatedComment, bookTitle));
  }

  @Test
  void deleteById() {
    String bookTitle = "title";
    String desiredCommentId = "76745";
    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
    Book book = Book.builder()
                    .title(bookTitle)
                    .comments(List.of(commentOne, commentTwo))
                    .build();

    when(bookDao.findByTitle(bookTitle)).thenReturn(Optional.of(book));

    commentService.deleteById(bookTitle, desiredCommentId);

    assertThat(book.getComments()).doesNotContain(commentTwo);
  }
}