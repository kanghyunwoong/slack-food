package com.slack.food.domain;


import java.util.StringJoiner;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.DynamicInsert;
import org.springframework.util.StringUtils;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@DynamicInsert
@Table(name = "location")
public class Location {

 @Id
 @GeneratedValue(strategy = GenerationType.IDENTITY)
 private Long seq;

 @Column(length = 100)
 private String channel; //채널 이름

 @Column
 private String user; //생성자

 @Column(length = 100)
 private String addressName;  //주소

 @Column(length = 100)
 private String categoryName; //카테고리 분류 음식점 > 한식 > 국수"

 @Column(length = 20)
 private String cateDepth1;

 @Column(length = 20)
 private String cateDepth2;

 @Column(length = 20)
 private String cateDepth3;

 @Column(length = 20)
 private String cateDepth4;

 @Column(length = 100)
 private String phone; //가게 번호

 @Column(length = 100)
 private String placeName; // 가게 이름

 @Column(length = 100)
 private String placeUrl; // 가게 홈페이지

 @Column(length = 100)
 private String mainImage;

 @Column(length = 10)
 private String scoreAvg;

 @Column(columnDefinition = "integer default 0")
 @Builder.Default
 private Integer good = 0; //좋아영


 public String getSubCategory() {
  StringJoiner sj = new StringJoiner(" > ");

  if(!StringUtils.isEmpty(this.cateDepth3)) {
   sj.add(this.cateDepth3);
  }
  if(!StringUtils.isEmpty(this.cateDepth4)) {
   sj.add(this.cateDepth4);
  }

  sj.setEmptyValue(this.cateDepth2);

  return sj.toString();
 }


}
