package com.study.spring.library.events;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Genre;
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
public class GenreEventListener extends AbstractMongoEventListener<Genre> {

  private final BookDao bookDao;
  private final GenreDao genreDao;

  @Override
  public void onBeforeDelete(BeforeDeleteEvent<Genre> event) {
    super.onBeforeDelete(event);
    Document genreDoc = event.getDocument();
    String genreId = genreDoc.get("_id").toString();
    if (!CollectionUtils.isEmpty(bookDao.findByGenre(genreId))) {
      throw new ConsistencyException("Cannot delete genre. There are books with that genre in database!");
    }
  }

  @Override
  public void onBeforeSave(BeforeSaveEvent<Genre> event) {
    super.onBeforeSave(event);
    Document genreDoc = event.getDocument();
    String genreName = genreDoc.get("name").toString();
    if (genreDao.findByName(genreName).isPresent()) {
      throw new ConsistencyException("Cannot create genre. There is another genre with that name in database!");
    }
  }
}
