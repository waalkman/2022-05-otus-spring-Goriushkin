package com.study.spring.library.routers;

import com.study.spring.library.exceptions.LibraryAppException;
import com.study.spring.library.handlers.AuthorHandler;
import com.study.spring.library.handlers.BookHandler;
import com.study.spring.library.handlers.CommentHandler;
import com.study.spring.library.handlers.GenreHandler;
import java.nio.charset.StandardCharsets;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.WebFilter;
import reactor.core.publisher.Mono;

@Configuration(proxyBeanMethods = false)
public class RoutersConfig {

  @Bean
  public RouterFunction<ServerResponse> routeAuthors(AuthorHandler authorHandler) {
    return RouterFunctions.route()
                          .GET("/api/v1/authors", RequestPredicates.accept(MediaType.APPLICATION_JSON), authorHandler::getAll)
                          .POST("/api/v1/authors", RequestPredicates.accept(MediaType.APPLICATION_JSON), authorHandler::create)
                          .PATCH("/api/v1/authors", RequestPredicates.accept(MediaType.APPLICATION_JSON), authorHandler::update)
                          .DELETE("/api/v1/authors/{id}", authorHandler::delete)
                          .build();
  }

  @Bean
  public RouterFunction<ServerResponse> routeGenres(GenreHandler genreHandler) {
    return RouterFunctions.route()
                          .GET("/api/v1/genres", RequestPredicates.accept(MediaType.APPLICATION_JSON), genreHandler::getAll)
                          .POST("/api/v1/genres", RequestPredicates.accept(MediaType.APPLICATION_JSON), genreHandler::create)
                          .PATCH("/api/v1/genres", RequestPredicates.accept(MediaType.APPLICATION_JSON), genreHandler::update)
                          .DELETE("/api/v1/genres/{id}", genreHandler::delete)
                          .build();
  }

  @Bean
  public RouterFunction<ServerResponse> routeBooks(BookHandler bookHandler) {
    return RouterFunctions.route()
                          .GET("/api/v1/books", RequestPredicates.accept(MediaType.APPLICATION_JSON), bookHandler::getAll)
                          .GET("/api/v1/books/{id}", RequestPredicates.accept(MediaType.APPLICATION_JSON), bookHandler::getById)
                          .POST("/api/v1/books", RequestPredicates.accept(MediaType.APPLICATION_JSON), bookHandler::create)
                          .PATCH("/api/v1/books", RequestPredicates.accept(MediaType.APPLICATION_JSON), bookHandler::update)
                          .DELETE("/api/v1/books/{id}", bookHandler::delete)
                          .build();
  }

  @Bean
  public RouterFunction<ServerResponse> routeComments(CommentHandler commentHandler) {
    return RouterFunctions.route()
                          .POST("/api/v1/books/{bookId}/comments", RequestPredicates.accept(MediaType.APPLICATION_JSON), commentHandler::create)
                          .PATCH("/api/v1/books/{bookId}/comments/{commentId}", RequestPredicates.accept(MediaType.APPLICATION_JSON), commentHandler::update)
                          .DELETE("/api/v1/books/{bookId}/comments/{commentId}", commentHandler::delete)
                          .build();
  }

  @Bean
  public WebFilter exceptionHandlerFilter() {
    return (exchange, next) -> next.filter(exchange)
                                   .onErrorResume(LibraryAppException.class, e -> {
                                     ServerHttpResponse response = exchange.getResponse();
                                     response.setStatusCode(HttpStatus.I_AM_A_TEAPOT);
                                     return response.writeWith(
                                         Mono.just(
                                             response.bufferFactory().wrap(
                                                 e.getMessage().getBytes(StandardCharsets.UTF_8))));
                                   });
  }
}
