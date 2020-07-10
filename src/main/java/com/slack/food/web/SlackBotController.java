package com.slack.food.web;

import com.fasterxml.jackson.databind.JsonNode;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.util.json.GsonFactory;
import com.slack.food.domain.Step;
import com.slack.food.service.KakaoMapService;
import com.slack.food.service.SlackBotService;
import com.slack.food.util.CommonUtil;
import com.slack.food.web.dto.Event;
import java.time.LocalDateTime;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/slack")
public class SlackBotController {

  private final SlackBotService slackBotService;
  private final KakaoMapService kakaoMapService;
  private final Step step;

  @PostMapping ("/events")
  public ResponseEntity<?> handleEvent(@RequestBody JsonNode jsonNode) throws Exception {
    switch (jsonNode.get("type").asText().toUpperCase()) {
      case "URL_VERIFICATION":
        return ResponseEntity.ok(jsonNode.get("challenge"));
      case "EVENT_CALLBACK":
        Event event = CommonUtil.convertDto(jsonNode.get("event"), Event.class);
        if(event.getText().contains("ㄱ")) {
          step.start(()-> {slackBotService.step1(event);},1L);
          step.start(()->{slackBotService.step2(event);},10L);  //60*5L
          step.start(() ->{slackBotService.step3(event);},20L); //60*5L
        }
        ResponseEntity.ok().build();
        return ResponseEntity.ok().build();
      default:
        return ResponseEntity.badRequest().build();
    }
  }

  @RequestMapping(value = "/vote", method = RequestMethod.POST)
  public ResponseEntity<?> voteEvent(@RequestParam String payload) {
    BlockActionPayload blockActionPayload = GsonFactory.createSnakeCase().fromJson(payload, BlockActionPayload.class);
    slackBotService.vote(blockActionPayload);
    return ResponseEntity.ok().build();
  }

  @RequestMapping(value = "/setting", method = RequestMethod.POST)
  public ResponseEntity<?> setting(@RequestParam(value = "channel_id") String channel,
                                   @RequestParam(value = "command") String command,
                                   @RequestParam(value = "text") String text){

    kakaoMapService.initSetting(channel,text);

    return ResponseEntity.ok().build();
  }
}
