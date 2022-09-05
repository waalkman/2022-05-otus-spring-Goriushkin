package com.study.spring.library.handlers;

import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.service.BookService;
import com.study.spring.library.service.CommentService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CommentHandler {

  private final BookService bookService;
  private final CommentService commentService;

  public Mono<List<Comment>> findByBook(String bookId) {
    return bookService.findById(bookId)
                      .map(Book::getComments);
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    String bookId = serverRequest.pathVariable("bookId");
    return serverRequest.bodyToMono(Comment.class)
                        .map(comment ->
                                 commentService.create(new Comment(comment.getText(), comment.getUserName()), bookId))
                        .flatMap(book ->
                                     ServerResponse.ok()
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .body(book, Book.class));
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {
    String bookId = serverRequest.pathVariable("bookId");
    return serverRequest.bodyToMono(Comment.class)
                        .map(comment -> commentService.update(comment, bookId))
                        .flatMap(book ->
                                     ServerResponse.ok()
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .body(book, Book.class));
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    String bookId = serverRequest.pathVariable("bookId");
    String commentId = serverRequest.pathVariable("commentId");
    return ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(commentService.deleteById(bookId, commentId), Void.class);
  }
}
