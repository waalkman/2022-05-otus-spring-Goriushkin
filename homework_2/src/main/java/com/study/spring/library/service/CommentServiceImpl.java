package com.study.spring.library.service;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.CommentDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
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
    return commentDao.getAll();
  }

  @Override
  @Transactional
  public long create(Comment comment, String bookTitle) {
    requestAndSetBook(comment, bookTitle);
    return commentDao.save(comment);
  }

  @Override
  public Comment getById(Long id) {
    return commentDao.getById(id);
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
    Book book = bookDao.getByTitle(bookTitle);
    comment.setBook(book);
  }
}
