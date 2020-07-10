package com.slack.food.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slack.food.domain.Location;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoReq {
  @JsonProperty("address_name")
  private String addressName;

  @JsonProperty("category_group_code")
  private String categoryGroupCode;

  @JsonProperty("category_group_name")
  private String categoryGroupName;

  @JsonProperty("category_name")
  private String categoryName;

  @JsonProperty("phone")
  private String phone;

  @JsonProperty("place_name")
  private String placeName;

  @JsonProperty("place_url")
  private String placeUrl;

  private String mainImage;
  private String scoreAvg;

  public Location toEntity(String channel) {

    String cate[] = {"","","",""};
    String[] spl = categoryName.split(">");
    for(int i =0; i< spl.length; i ++) {
      cate[i] = spl[i].trim();
    }

   return Location.builder()
       .addressName(addressName)
       .categoryName(categoryName)
       .cateDepth1(cate[0])
       .cateDepth2(cate[1])
       .cateDepth3(cate[2])
       .cateDepth4(cate[3])
       .phone(phone)
       .mainImage(mainImage)
       .scoreAvg(scoreAvg)
       .channel(channel)
       .placeName(placeName)
       .placeUrl(placeUrl)
       .build();
  }
}
