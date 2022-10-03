package com.study.spring.library.service;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

  private final BookDao bookDao;

  @Override
  public Mono<Book> create(Comment comment, String bookId) {
    return bookDao.findById(bookId)
                  .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found", "Book")))
                  .flatMap(book -> {
                    if (book.getComments() == null) {
                      book.setComments(new ArrayList<>());
                    }
                    book.getComments().add(comment);
                    return bookDao.save(book);
                  });
  }

  @Override
  public Mono<Book> update(Comment comment, String bookId) {
    return bookDao.findById(bookId)
                  .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found", "Book")))
                  .doOnNext(book -> {
                    Comment foundComment = book.getComments()
                                               .stream()
                                               .filter(c -> c.getId().equals(comment.getId()))
                                               .findAny()
                                               .orElseThrow(() -> new EntityNotFoundException("Comment not found", "Comment"));

                    foundComment.setUserName(comment.getUserName());
                    foundComment.setText(comment.getText());
                  })
                  .flatMap(bookDao::save);
  }

  @Override
  public Mono<Book> deleteById(String bookId, String id) {
    return bookDao.findById(bookId)
                  .switchIfEmpty(Mono.error(new EntityNotFoundException("Book not found", "Book")))
                  .doOnNext(book -> {
                    book.setComments(
                        book.getComments()
                            .stream()
                            .filter(c -> !c.getId().equals(id))
                            .collect(Collectors.toList()));
                  })
                  .flatMap(bookDao::save);
  }
}
