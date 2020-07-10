package com.slack.food.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Category {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long seq;

  @Column(length = 100)
  private String channel; //채널 이름

  @Column
  private String user; //생성자

  @Column(length = 50)
  private String category; //카테고리

  @Column(columnDefinition = "integer default 0")
  @Builder.Default
  private Integer good = 0;


}
