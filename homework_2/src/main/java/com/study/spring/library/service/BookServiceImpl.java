package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.dao.GenreDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.exceptions.ConsistencyException;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookServiceImpl implements BookService {

  private final BookDao bookDao;
  private final AuthorDao authorDao;
  private final GenreDao genreDao;
  private final MutableAclService mutableAclService;

  @Override
  public Collection<Book> getAll() {
    return bookDao.findAll();
  }

  @Override
  public Book create(Book book, String genre, String author) {
    book.setId(new ObjectId().toString());
    checkAuthorAndGenre(book, genre, author);
    book.setComments(new ArrayList<>());
    createAclEntry(book);
    return save(book);
  }

  @Override
  public Book findById(String id) {
    return bookDao.findById(id)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
  }

  @Override
  public Book findByTitle(String title) {
    return bookDao.findByTitle(title)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
  }

  @Override
  public void update(Book book, String genre, String author) {
    Book currentBook = bookDao.findById(book.getId())
                              .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));

    checkAuthorAndGenre(book, genre, author);
    currentBook.setTitle(book.getTitle());
    currentBook.setDescription(book.getDescription());
    currentBook.setAuthor(book.getAuthor());
    currentBook.setGenre(book.getGenre());

    save(currentBook);
  }

  private Book save(Book book) {
    if (bookDao.existsByTitle(book.getTitle())) {
      throw new ConsistencyException("Cannot create book. There is another book with that title in database!");
    } else {
      return bookDao.save(book);
    }
  }

  private void createAclEntry(Book book) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final Sid owner = new PrincipalSid(authentication);
    ObjectIdentity oid = new ObjectIdentityImpl("com.study.spring.library.domain.Book", book.getId());
    final Sid admin = new PrincipalSid("admin");

    MutableAcl acl = mutableAclService.createAcl(oid);
    acl.setOwner(owner);
    acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, owner, true);
    acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, admin, true);

    mutableAclService.updateAcl(acl);
  }

  @Override
  public void deleteById(String id) {
    bookDao.deleteById(id);
  }

  private void checkAuthorAndGenre(Book book, String genre, String author) {
    book.setGenre(genreDao.findByName(genre).orElseThrow(() -> new EntityNotFoundException("Genre not found", "Genre")));
    book.setAuthor(authorDao.findByName(author).orElseThrow(() -> new EntityNotFoundException("Author not found", "Author")));
  }
}
