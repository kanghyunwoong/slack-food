package com.slack.food.service;

import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.food.web.dto.Event;

public interface SlackBotService {


  void step1(Event event);

  void step2(Event event);

  void step3(Event event);

  void vote(BlockActionPayload blockActionPayload);

  void send(String channel,String text);

}
