package com.study.spring.library.service;

import com.study.spring.library.dao.AuthorDao;
import com.study.spring.library.dao.BookDao;
import com.study.spring.library.domain.Author;
import com.study.spring.library.exceptions.ConsistencyException;
import com.study.spring.library.exceptions.EntityNotFoundException;
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
public class AuthorServiceImpl implements AuthorService {

  private final AuthorDao authorDao;
  private final BookDao bookDao;
  private final MutableAclService mutableAclService;

  @Override
  public Collection<Author> getAll() {
    return authorDao.findAll();
  }

  @Override
  public Author create(Author author) {
    author.setId(new ObjectId().toString());
    createAclEntry(author);
    return save(author);
  }

  @Override
  public Author findById(String id) {
    return authorDao.findById(id)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found", "Author"));
  }

  @Override
  public Author findByName(String name) {
    return authorDao.findByName(name)
                    .orElseThrow(() -> new EntityNotFoundException("Author not found", "Author"));
  }

  @Override
  public void update(Author author) {
    save(author);
  }

  private Author save(Author author) {
    if (authorDao.existsByName(author.getName())) {
      throw new ConsistencyException("Cannot create author. There is another author with that name in database!");
    } else {
      return authorDao.save(author);
    }
  }

  private void createAclEntry(Author author) {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    final Sid owner = new PrincipalSid(authentication);
    ObjectIdentity oid = new ObjectIdentityImpl("com.study.spring.library.domain.Author", author.getId());
    final Sid admin = new PrincipalSid("admin");

    MutableAcl acl = mutableAclService.createAcl(oid);
    acl.setOwner(owner);
    acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, owner, true);
    acl.insertAce(acl.getEntries().size(), BasePermission.ADMINISTRATION, admin, true);

    mutableAclService.updateAcl(acl);
  }

  @Override
  public void deleteById(String id) {
    if (bookDao.existsByAuthorId(id)) {
      throw new ConsistencyException("Cannot delete author. There are book(s) with that author in database!");
    } else {
      authorDao.deleteById(id);
    }
  }
}
