//package com.study.spring.library.service;
//
//import static org.assertj.core.api.Assertions.assertThat;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertThrows;
//import static org.mockito.ArgumentMatchers.any;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import com.study.spring.library.dao.BookDao;
//import com.study.spring.library.domain.Book;
//import com.study.spring.library.domain.Comment;
//import com.study.spring.library.exceptions.EntityNotFoundException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Optional;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//
//@ExtendWith(MockitoExtension.class)
//class CommentServiceImplTest {
//
//  @Mock
//  private BookDao bookDao;
//  @InjectMocks
//  private CommentServiceImpl commentService;
//
//  @Test
//  void create_success() {
//    String bookId = "2839tbcoiqfg";
//    Comment comment = Comment.builder().text("text").userName("user").id("1232").build();
//    Book expectedSaveBook = Book.builder().comments(List.of(comment)).build();
//
//    when(bookDao.findById(bookId)).thenReturn(Optional.of(Book.builder().comments(new ArrayList<>()).build()));
//
//    commentService.create(comment, bookId);
//
//    verify(bookDao).findById(bookId);
//    verify(bookDao).save(expectedSaveBook);
//  }
//
//  @Test
//  void create_bookNotFound() {
//    String bookId = "2839tbcoiqfg";
//
//    when(bookDao.findById(bookId)).thenReturn(Optional.empty());
//
//    assertThrows(EntityNotFoundException.class, () -> commentService.create(null, bookId));
//  }
//
//  @Test
//  void findById_success() {
//    String bookId = "2839tbcoiqfg";
//    String desiredCommentId = "76745";
//    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
//    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
//    Book book = Book.builder()
//                    .title(bookId)
//                    .comments(List.of(commentOne, commentTwo))
//                    .build();
//
//    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
//
//    Comment actualComment = commentService.findById(bookId, desiredCommentId);
//
//    assertEquals(commentTwo, actualComment);
//  }
//
//  @Test
//  void findById_bookNotFound() {
//    when(bookDao.findById(any())).thenReturn(Optional.empty());
//
//    assertThrows(EntityNotFoundException.class, () -> commentService.findById(null, null));
//  }
//
//  @Test
//  void findById_commentNotFound() {
//    String bookId = "2839tbcoiqfg";
//    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
//    Book book = Book.builder()
//                    .id(bookId)
//                    .title("title")
//                    .comments(List.of(commentOne))
//                    .build();
//
//    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
//
//    assertThrows(EntityNotFoundException.class, () -> commentService.findById(bookId, "nu11"));
//  }
//
//  @Test
//  void findByBookTitle_success() {
//    String bookId = "2839tbcoiqfg";
//    String desiredCommentId = "76745";
//    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
//    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
//    List<Comment> expectedBookComments = List.of(commentOne, commentTwo);
//    Book book = Book.builder()
//                    .id(bookId)
//                    .title("dfioyuwvc")
//                    .comments(expectedBookComments)
//                    .build();
//
//    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
//
//    Collection<Comment> actualComments = commentService.findByBook(bookId);
//
//    assertThat(actualComments).containsExactlyElementsOf(expectedBookComments);
//  }
//
//  @Test
//  void findByBookTitle_bookNotFound() {
//    when(bookDao.findById(any())).thenReturn(Optional.empty());
//
//    assertThrows(EntityNotFoundException.class, () -> commentService.findByBook("nu11"));
//  }
//
//  @Test
//  void update_success() {
//    String bookId = "2839tbcoiqfg";
//    String desiredCommentId = "76745";
//    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
//    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
//    Comment updatedComment = Comment.builder().text("new_label").userName("god").id(desiredCommentId).build();
//    Book book = Book.builder()
//                    .id(bookId)
//                    .title("daugfcvouwyp")
//                    .comments(List.of(commentOne, commentTwo))
//                    .build();
//
//    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
//
//    commentService.update(updatedComment, bookId);
//
//    assertThat(book.getComments()).contains(updatedComment);
//    assertThat(book.getComments()).doesNotContain(commentTwo);
//  }
//
//  @Test
//  void update_commentNotFound() {
//    String bookId = "2839tbcoiqfg";
//    String desiredCommentId = "76745";
//    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
//    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
//    Comment updatedComment = Comment.builder().text("new_label").userName("god").id("where is it?").build();
//    Book book = Book.builder()
//                    .id(bookId)
//                    .title("cwoiycvip")
//                    .comments(List.of(commentOne, commentTwo))
//                    .build();
//
//    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
//
//    assertThrows(EntityNotFoundException.class, () -> commentService.update(updatedComment, bookId));
//  }
//
//  @Test
//  void deleteById() {
//    String bookId = "2839tbcoiqfg";
//    String desiredCommentId = "76745";
//    Comment commentOne = Comment.builder().text("text").userName("user").id("1232").build();
//    Comment commentTwo = Comment.builder().text("label").userName("admin").id(desiredCommentId).build();
//    Book book = Book.builder()
//                    .id(bookId)
//                    .title("cupwb")
//                    .comments(List.of(commentOne, commentTwo))
//                    .build();
//
//    when(bookDao.findById(bookId)).thenReturn(Optional.of(book));
//
//    commentService.deleteById(bookId, desiredCommentId);
//
//    assertThat(book.getComments()).doesNotContain(commentTwo);
//  }
//}