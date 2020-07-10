package com.slack.food.web.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategy.SnakeCaseStrategy.class)
public class SlashPayload {
  private String token;

  private String team_id;
  private String teamDomain;
  private String enterpriseId;
  private String enterpriseName;
  private String channelId;
  private String channelName;
  private String userId;
  private String userName;
  private String command;
  private String text;
  private String responseUrl;
  private String triggerId;
}
