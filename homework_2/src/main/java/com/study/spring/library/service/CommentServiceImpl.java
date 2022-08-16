package com.study.spring.library.service;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final BookDao bookDao;

  @Override
  public void create(Comment comment, String bookTitle) {
    Book book = getBook(bookTitle);
    book.getComments().add(comment);
    bookDao.save(book);
  }

  @Override
  public Comment findById(String bookTitle, String id) {
    return bookDao.findByTitle(bookTitle)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"))
                  .getComments()
                  .stream()
                  .filter(c -> c.getId().equals(id))
                  .findAny()
                  .orElseThrow(() -> new EntityNotFoundException("Comment not found", "Comment"));
  }

  @Override
  public Collection<Comment> findByBookTitle(String bookTitle) {
    return bookDao.findByTitle(bookTitle)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"))
                  .getComments();
  }

  @Override
  public void update(Comment comment, String bookTitle) {
    Book book = getBook(bookTitle);
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
  public void deleteById(String bookTitle, String id) {
    Book book = getBook(bookTitle);
    book.setComments(
        book.getComments()
            .stream()
            .filter(c -> !c.getId().equals(id))
            .collect(Collectors.toList()));

    bookDao.save(book);
  }

  private Book getBook(String bookTitle) {
    return bookDao.findByTitle(bookTitle)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
  }
}
