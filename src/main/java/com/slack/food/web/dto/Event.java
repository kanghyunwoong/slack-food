package com.slack.food.web.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class Event {

  private String type;
  private String channel;
  private String text;
  private String user;
  private String ts;

  @Builder
  public Event (String type,String channel, String text, String user,String ts) {
    this.type = type;
    this.ts = ts;
    this.channel = channel;
    this.text = text;
    this.user = user;
  }

}
