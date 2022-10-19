package com.study.spring.library.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final BookDao bookDao;

  @Override
  @HystrixCommand(groupKey = "CommentService")
  public void create(Comment comment, String bookId) {
    Book book = getBook(bookId);
    if (book.getComments() == null) {
      book.setComments(new ArrayList<>());
    }
    book.getComments().add(comment);
    bookDao.save(book);
  }

  @Override
  @HystrixCommand(groupKey = "CommentService", fallbackMethod = "fallbackComment")
  public Comment findById(String bookId, String id) {
    return bookDao.findById(bookId)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"))
                  .getComments()
                  .stream()
                  .filter(c -> c.getId().equals(id))
                  .findAny()
                  .orElseThrow(() -> new EntityNotFoundException("Comment not found", "Comment"));
  }

  @Override
  @HystrixCommand(groupKey = "CommentService", fallbackMethod = "getCommentsFallback")
  public Collection<Comment> findByBook(String bookId) {
    return bookDao.findById(bookId)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"))
                  .getComments();
  }

  @Override
  @HystrixCommand(groupKey = "CommentService")
  public void update(Comment comment, String bookId) {
    Book book = getBook(bookId);
    book.setComments(
        book.getComments()
            .stream()
            .map(Comment::copy)
            .collect(Collectors.toList()));

    Comment foundComment = book.getComments()
                               .stream()
                               .filter(c -> c.getId().equals(comment.getId()))
                               .findAny()
                               .orElseThrow(() -> new EntityNotFoundException("Comment not found", "Comment"));

    foundComment.setUserName(comment.getUserName());
    foundComment.setText(comment.getText());

    bookDao.save(book);
  }

  @Override
  @HystrixCommand(groupKey = "CommentService")
  public void deleteById(String bookId, String id) {
    Book book = getBook(bookId);
    book.setComments(
        book.getComments()
            .stream()
            .filter(c -> !c.getId().equals(id))
            .collect(Collectors.toList()));

    bookDao.save(book);
  }

  private Book getBook(String bookId) {
    return bookDao.findById(bookId)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
  }

  private Collection<Comment> getCommentsFallback(String bookId) {
    return Collections.singletonList(fallbackComment(bookId, "Fallback comment id"));
  }

  private Comment fallbackComment(String bookId, String id) {
    return Comment.builder()
                  .id("Fallback comment id")
                  .text("Fallback comment text")
                  .userName("Fallback user name")
                  .build();
  }
}
