package com.study.spring.library.service;

import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Book;
import com.study.spring.library.domain.Comment;
import com.study.spring.library.exceptions.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
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
public class CommentServiceImpl implements CommentService {

  private final BookDao bookDao;
  private final MutableAclService mutableAclService;

  @Override
  public void create(Comment comment, String bookId) {
    Book book = getBook(bookId);
    if (book.getComments() == null) {
      book.setComments(new ArrayList<>());
    }
    book.getComments().add(comment);
    createAclEntry(comment);
    bookDao.save(book);
  }

  @Override
  public Comment findById(String bookId, String id) {
    return bookDao.findById(bookId)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"))
                  .getComments()
                  .stream()
                  .filter(c -> c.getId().equals(id))
                  .findAny()
                  .orElseThrow(() -> new EntityNotFoundException("Comment not found", "Comment"));
  }

  @Override
  public Collection<Comment> findByBook(String bookId) {
    return bookDao.findById(bookId)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"))
                  .getComments();
  }

  @Override
  public void update(Comment comment, String bookId) {
    Book book = getBook(bookId);
    book.setComments(
        book.getComments()
            .stream()
            .map(Comment::copy)
            .collect(Collectors.toList()));

    Comment foundComment = book.getComments()
                               .stream()
                               .filter(c -> c.getId().equals(comment.getId()))
                               .findAny()
                               .orElseThrow(() -> new EntityNotFoundException("Comment not found", "Comment"));

    foundComment.setUserName(comment.getUserName());
    foundComment.setText(comment.getText());

    bookDao.save(book);
  }

  @Override
  public void deleteById(String bookId, String id) {
    Book book = getBook(bookId);
    book.setComments(
        book.getComments()
            .stream()
            .filter(c -> !c.getId().equals(id))
            .collect(Collectors.toList()));

    bookDao.save(book);
  }

  private void createAclEntry(Comment comment) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final Sid owner = new PrincipalSid(authentication);
    ObjectIdentity oid = new ObjectIdentityImpl("com.study.spring.library.domain.Comment", comment.getId());
    final Sid admin = new PrincipalSid("admin");

    MutableAcl acl = mutableAclService.createAcl(oid);
    acl.setOwner(owner);
    acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, owner, true);
    acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, admin, true);

    mutableAclService.updateAcl(acl);
  }

  private Book getBook(String bookId) {
    return bookDao.findById(bookId)
                  .orElseThrow(() -> new EntityNotFoundException("Book not found", "Book"));
  }
}
