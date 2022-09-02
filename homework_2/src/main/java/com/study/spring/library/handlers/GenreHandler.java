package com.study.spring.library.handlers;

import com.study.spring.library.domain.Genre;
import com.study.spring.library.service.GenreService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GenreHandler {

  private final GenreService genreService;

  public Mono<ServerResponse> getAll(ServerRequest serverRequest) {
    return ServerResponse.ok()
        .contentType(MediaType.APPLICATION_JSON)
        .body(genreService.findAll(), Genre.class);
  }

  public Mono<ServerResponse> create(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Genre.class)
                        .flatMap(author ->
                                     ServerResponse.ok()
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .body(genreService.create(author), Genre.class));
  }

  public Mono<ServerResponse> update(ServerRequest serverRequest) {
    return serverRequest.bodyToMono(Genre.class)
                        .flatMap(author ->
                                     ServerResponse.ok()
                                                   .contentType(MediaType.APPLICATION_JSON)
                                                   .body(genreService.update(author), Genre.class));
  }

  public Mono<ServerResponse> delete(ServerRequest serverRequest) {
    String id = serverRequest.pathVariable("id");
    return ServerResponse.ok()
                         .contentType(MediaType.APPLICATION_JSON)
                         .body(genreService.deleteById(id), Void.class);
  }
}
