package com.study.spring.library.handlers;

import com.study.spring.library.domain.Author;
import com.study.spring.library.service.AuthorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class AuthorHandler {

  private final AuthorService authorService;

  public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(authorService.findAll(), Author.class);
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Author.class)
                        .map(authorService::create)
                        .flatMap(author ->
                                     ServerResponse.ok()
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .body(author, Author.class));
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Author.class)
                        .map(authorService::update)
                        .flatMap(author ->
                                     ServerResponse.ok()
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .body(author, Author.class));
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    String id = serverRequest.pathVariable("id");
    return ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(authorService.deleteById(id), Void.class);
  }
}
