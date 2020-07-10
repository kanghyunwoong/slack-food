package com.slack.food.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class CommonUtil {

  public static <T> T convertDto(JsonNode jsonNode, Class<T> tClass) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.treeToValue(jsonNode,tClass);
  }

  public static <T> T convertDto(String st, Class<T> tClass) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    objectMapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
    return objectMapper.readValue(st, tClass);
  }


}
