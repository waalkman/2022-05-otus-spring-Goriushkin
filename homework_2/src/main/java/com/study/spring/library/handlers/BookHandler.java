package com.study.spring.library.handlers;

import com.study.spring.library.domain.Book;
import com.study.spring.library.dto.BookDto;
import com.study.spring.library.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class BookHandler {

  private final BookService bookService;

  public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(bookService.findAll(), Book.class);
  }

  public Mono<ServerResponse> getById(ServerRequest serverRequest) {
    String id = serverRequest.pathVariable("id");
    return ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(bookService.findById(id), Book.class);
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(BookDto.class)
                        .map(bookDto -> bookService.create(bookDto.toBook(), bookDto.getGenre(), bookDto.getAuthor()))
                        .flatMap(book ->
                                     ServerResponse.ok()
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .body(book, Book.class));
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(BookDto.class)
                        .map(bookDto -> bookService.create(bookDto.toBook(), bookDto.getGenre(), bookDto.getAuthor()))
                        .flatMap(book ->
                                     ServerResponse.ok()
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .body(book, Book.class));
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    String id = serverRequest.pathVariable("id");
    return ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(bookService.deleteById(id), Void.class);
  }
}
