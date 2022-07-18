package com.study.spring.library.map;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.PrintableBook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookMapper implements EntityMapper<Book, PrintableBook> {

  private final GenreDao genreDao;
  private final AuthorDao authorDao;

  @Override
  public PrintableBook map(Book book) {
    String genre = genreDao.getById(book.getGenreId()).getName();
    String author = authorDao.getById(book.getAuthorId()).getName();

    return PrintableBook.builder()
                        .id(book.getId())
                        .title(book.getTitle())
                        .description(book.getDescription())
                        .author(author)
                        .genre(genre)
                        .build();
  }

}
