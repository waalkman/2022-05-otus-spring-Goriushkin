package com.study.spring.library.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@Table(name = "comments")
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Comment {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;
  private String text;
  @Column(name = "user_name")
  private String userName;
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  @JoinColumn(name = "book_id")
  @ManyToOne
  private Book book;

}
