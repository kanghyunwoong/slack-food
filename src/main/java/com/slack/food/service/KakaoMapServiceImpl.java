package com.slack.food.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.slack.api.Slack;
import com.slack.food.domain.Category;
import com.slack.food.domain.Location;
import com.slack.food.repository.CategoryRepository;
import com.slack.food.repository.LocationRepositoy;
import com.slack.food.util.CommonUtil;
import com.slack.food.web.dto.KakaoReq;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Transactional
public class KakaoMapServiceImpl implements KakaoMapService{

  private final RestTemplate kakaoRestTemplate;
  private final RestTemplate restTemplate;

  private final SlackBotService slackBotService;
  private final LocationRepositoy locationRepositoy;
  private final CategoryRepository categoryRepository;

  @Override
  @Async
  public void initSetting(String channel, String text) {

    try {
      Thread.sleep(1000);

      final String distance = "500";

      //데이터 삭제
      this.resetData(channel);

      //1. 위치 x,y 좌표
      Map<String,String> location = this.regCurrentLocation(text);
      String x = location.get("x");
      String y = location.get("y");

      //등록된 현재 위치로 500m 내 리스트 뽑기
      this.settingFoodList(channel, x, y, distance);

      slackBotService.send(channel,"`"+text+" 등록 되었습니다.`");

    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public Map<String,String> regCurrentLocation(String text) {
    Map<String,String> result = new HashMap<>();

    String url = "https://dapi.kakao.com/v2/local/search/address.json?query="+text;

    ResponseEntity<JsonNode> response = kakaoRestTemplate.getForEntity(url, JsonNode.class);
    JsonNode jsonNode = response.getBody().get("documents");

    if(jsonNode.isEmpty()) {
      //Exception
    }

    result.put("x",jsonNode.get(0).get("x").asText());
    result.put("y",jsonNode.get(0).get("y").asText());

    return result;
  }

  public void settingFoodList(String channel,String x, String y, String distance) {

    StringBuilder builder = new StringBuilder();
    builder.append("https://dapi.kakao.com/v2/local/search/category.json?")
        .append("radius="+distance)
        .append("&category_group_code=FD6")
        .append("&x="+x)
        .append("&y="+y);

    try {

      ResponseEntity<JsonNode> response = kakaoRestTemplate.getForEntity(builder.toString(), JsonNode.class);
      CommonUtil.convertDto(response.getBody().get("documents").toString(),KakaoReq[].class);
      KakaoReq[] kakaoArg = CommonUtil.convertDto(response.getBody().get("documents").toString(),KakaoReq[].class);

      List<Location> locations = Arrays.asList(kakaoArg).stream()
          .map(kakaoReq -> addImageUrlAndScoreAvg(kakaoReq))
          .map(kakaoReq -> kakaoReq.toEntity(channel))
          .collect(Collectors.toList());

      locations.stream().map(location -> location.getCateDepth2()).distinct().forEach(category -> {
        categoryRepository.save(
            Category.builder()
                .category(category)
                .user("")
                .channel(channel)
                .build()
        );
      });

      locationRepositoy.saveAll(locations);

    }catch (Exception e) {

    }
  }

  private KakaoReq addImageUrlAndScoreAvg(KakaoReq kakaoReq) {
    String placeUrl = kakaoReq.getPlaceUrl();
    String param = placeUrl.substring(placeUrl.lastIndexOf("/"));
    final String url = "https://place.map.kakao.com/m/main/v/"+param;

    ResponseEntity<JsonNode> response = restTemplate.getForEntity(url,JsonNode.class);
    JsonNode jsonNode = response.getBody();


    String mainImage = "";

    if(jsonNode.get("basicInfo").has("mainphotourl")) {
      mainImage = jsonNode.get("basicInfo").get("mainphotourl").asText();
    }

    Double scoreSum = jsonNode.get("comment").get("scoresum").asDouble();
    Double scoreCnt = jsonNode.get("comment").get("scorecnt").asDouble();

    DecimalFormat decimalFormat = new DecimalFormat(".#");
    String scoreAvg = decimalFormat.format(scoreSum/scoreCnt);

    kakaoReq.setMainImage(mainImage);
    kakaoReq.setScoreAvg(scoreAvg);

    return kakaoReq;
  }

  private void resetData(String channel) {

    locationRepositoy.deleteByChannel(channel);
    categoryRepository.deleteByChannel(channel);
  }


}
