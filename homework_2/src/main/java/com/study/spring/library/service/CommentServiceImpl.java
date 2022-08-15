package com.study.spring.library.service;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.CommentDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final CommentDao commentDao;
  private final BookDao bookDao;

  @Override
  public Collection<Comment> getAll() {
    return commentDao.findAll();
  }

  @Override
  @Transactional
  public Comment create(Comment comment, String bookTitle) {
    requestAndSetBook(comment, bookTitle);
    return commentDao.save(comment);
  }

  @Override
  public Comment findById(Long id) {
    return commentDao.findById(id)
                     .orElseThrow(() -> new EntityNotFoundException("Comment not found", "Comment"));
  }

  @Override
  @Transactional
  public void update(Comment comment, String bookTitle) {
    requestAndSetBook(comment, bookTitle);
    commentDao.save(comment);
  }

  @Override
  @Transactional
  public void deleteById(Long id) {
    commentDao.deleteById(id);
  }

  private void requestAndSetBook(Comment comment, String bookTitle) {
    Book book = bookDao.findByTitle(bookTitle)
                       .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
    comment.setBook(book);
  }
}
