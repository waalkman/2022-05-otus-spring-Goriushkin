package com.study.spring.library.events;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.exceptions.ConsistencyException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BookEventListener extends AbstractMongoEventListener<Book> {

  private final BookDao bookDao;

  @Override
  public void onBeforeSave(BeforeSaveEvent<Book> event) {
    super.onBeforeSave(event);
    Document bookDoc = event.getDocument();
    String bookTitle = bookDoc.get("title").toString();
    Optional<Book> book = bookDao.findByTitle(bookTitle);
    if (book.isPresent() && event.getSource().getId() == null) {
      throw new ConsistencyException("Cannot create book. There is another book with that title in database!");
    }
  }
}
