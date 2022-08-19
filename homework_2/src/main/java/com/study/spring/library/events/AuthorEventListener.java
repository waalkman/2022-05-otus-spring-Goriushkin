package com.study.spring.library.events;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.ConsistencyException;
import lombok.RequiredArgsConstructor;
import org.bson.Document;
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener;
import org.springframework.data.mongodb.core.mapping.event.BeforeDeleteEvent;
import org.springframework.data.mongodb.core.mapping.event.BeforeSaveEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

@Component
@RequiredArgsConstructor
public class AuthorEventListener extends AbstractMongoEventListener<Author> {

  private final BookDao bookDao;
  private final AuthorDao authorDao;

  @Override
  public void onBeforeDelete(BeforeDeleteEvent<Author> event) {
    super.onBeforeDelete(event);
    Document authorDoc = event.getDocument();
    String authorId = authorDoc.get("_id").toString();
    if (!CollectionUtils.isEmpty(bookDao.findByAuthor(authorId))) {
      throw new ConsistencyException("Cannot delete author. There are book(s) with that author in database!");
    }
  }

  @Override
  public void onBeforeSave(BeforeSaveEvent<Author> event) {
    super.onBeforeSave(event);
    Document authorDoc = event.getDocument();
    String authorName = authorDoc.get("name").toString();
    if (authorDao.findByName(authorName).isPresent()) {
      throw new ConsistencyException("Cannot create author. There is another author with that name in database!");
    }
  }
}
