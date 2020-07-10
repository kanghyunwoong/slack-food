package com.slack.food.service;

import static com.slack.api.model.block.Blocks.*;
import static com.slack.api.model.block.composition.BlockCompositions.*;
import static com.slack.api.model.block.element.BlockElements.button;

import com.slack.api.Slack;
import com.slack.api.app_backend.interactive_components.payload.BlockActionPayload;
import com.slack.api.methods.response.chat.ChatPostMessageResponse;
import com.slack.api.model.Attachment;
import com.slack.api.model.block.ActionsBlock;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.element.BlockElement;
import com.slack.api.model.block.element.BlockElements;
import com.slack.api.model.block.element.ButtonElement;
import com.slack.api.model.block.element.ImageElement;
import com.slack.food.config.SlackBotReq;
import com.slack.food.domain.Category;
import com.slack.food.domain.Location;
import com.slack.food.repository.CategoryRepository;
import com.slack.food.repository.LocationRepositoy;
import com.slack.food.web.dto.Event;
import java.awt.Color;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.StringJoiner;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class SlackBotServiceImpl implements SlackBotService{

  private final LocationRepositoy locationRepositoy;
  private final CategoryRepository categoryRepository;

  @Value("${slack.token}")
  private String slackToken;

  @Override
  public void step1(Event event) {
    String ts = send(event.getChannel(), this.settingCategory(event.getChannel()));
    event.setTs(ts);
  }

  @Override
  public void step2(Event event) {
    Category category = categoryRepository.findFirstByChannelOrderByGoodDesc(event.getChannel()).get();
    List<Location> locations = locationRepositoy.findAllByChannelAndCateDepth2(category.getChannel(), category.getCategory());

    List<LayoutBlock> layoutBlocks = new ArrayList<>();
    layoutBlocks.add(
        SectionBlock.builder()
            .text(markdownText("음식점 > "+category.getCategory()))
            .build());


    layoutBlocks.add(divider());

    List<Attachment> attachments = this.settingFoodListAttachment(locations,true);

    updateSend(event.getChannel(),attachments,layoutBlocks,event.getTs());
  }

  @Override
  public void step3(Event event) {
    Location location = locationRepositoy.findFirstByChannelOrderByGoodDesc(event.getChannel()).get();
    String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy년 MM월 dd일"));

    List<LayoutBlock> layoutBlocks = new ArrayList<>();
    layoutBlocks.add(
        SectionBlock.builder()
            .text(markdownText(new StringJoiner(" ").add(today).add("메뉴").toString()))
            .build()
            );

    layoutBlocks.add(divider());
    List<Attachment> attachments = this.settingFoodListAttachment(Arrays.asList(location),false);

    updateSend(event.getChannel(),attachments,layoutBlocks,event.getTs());

    this.initVoteData(event.getChannel());
  }

  @Override
  public void vote(BlockActionPayload blockActionPayload) {
    final String channel = blockActionPayload.getChannel().getId();
    final String ts = blockActionPayload.getMessage().getTs();

    List<LayoutBlock> layoutBlocks = new ArrayList<>();
    List<Attachment> attachments = new ArrayList<>();

      blockActionPayload.getActions().forEach(action -> {
      String value = action.getValue();
      Long actionId = Long.parseLong(action.getActionId());

      if(value.equals("CATEGORY")) {
        Category category = categoryRepository.findBySeq(actionId).get();
        category.setGood(category.getGood()+1);
        updateSend(channel,attachments,this.settingCategory(channel),ts);
      }

      else if(value.equals("LIST")) {
        Location location = locationRepositoy.findBySeq(actionId).get();
        location.setGood(location.getGood()+1);
        updateSend(channel,attachments,layoutBlocks,ts);
      }

    });

    //updateSend(channel,attachments,this.settingCategory(channel),ts);
    //updateSend(channel,attachments,blockActionPayload.getMessage().getBlocks(),ts);
  }


  private List<LayoutBlock> settingCategory(String channel) {
    List<LayoutBlock> layoutBlocks = new ArrayList<>();
    List<BlockElement> blockElements = new ArrayList<>();

    List<Category> categories = categoryRepository.findAllByChannel(channel);

    categories.stream().forEach(cate -> {
      String seq = String.valueOf(cate.getSeq());
      blockElements.add(
          button(b -> b.text(plainText(pt->pt.emoji(true).text(
              new StringBuilder()
                  .append(cate.getCategory())
                  .append("("+cate.getGood()+")")
                  .toString()
          ))).actionId(seq)
              .value("CATEGORY"))
      );
    });

    layoutBlocks.add(actions(action -> action.elements(blockElements)));

    return layoutBlocks;
  }

  private List<Attachment> settingFoodListAttachment(List<Location> locations,boolean activeButton) {
    List<Attachment> attachments = new ArrayList<>();


    locations.stream().forEach(location -> {
      List<LayoutBlock> att = new ArrayList<>();

      StringBuilder text = new StringBuilder();
      text.append("*<")
          .append(location.getPlaceUrl()+"|")
          .append(location.getPlaceName())
          .append(">*").append("\n")
          .append(location.getSubCategory()).append("\n")
          .append(makeStart(location.getScoreAvg()))
          .append(" ")
          .append(location.getScoreAvg()+"점").append("\n")
          .append(location.getAddressName()).append(" ").append("/").append(" ")
          .append(location.getPhone()).append("\n");

      att.add(
          SectionBlock.builder()
              .text(markdownText(text.toString()))
              .accessory(
                  ImageElement.builder().imageUrl(location.getMainImage()).altText("할루라라").build()
              )
              .build()
      );

      if(activeButton){
        att.add(
            ActionsBlock.builder()
                .elements(
                    BlockElements.asElements(
                        ButtonElement.builder()
                            .text(plainText(":thumbsup: Good (0)",true))
                            .actionId(String.valueOf(location.getSeq()))
                            .value("LIST")
                            .build()
                    )
                ).build());

        att.add(divider());
      }

      attachments.add(Attachment.builder()
          .blocks(att)
          .color(makeRandomColor())
          .build());
    });


    return attachments;
  }

  private String makeStart(String scoreAvg) {
    String result = "";
    int blackStar = (int) Math.floor(Double.parseDouble(scoreAvg));

    for(int i =0; i < 5; i ++) {
      if(blackStar > i) {
        result += "★";
      }else {
        result += "☆";
      }
    }
    return result;
  }

  private String makeRandomColor() {
    Random rand = new Random();
    float r = rand.nextFloat();
    float g = rand.nextFloat();
    float b = rand.nextFloat();

    Color randomColor = new Color(r, g, b);

    return "#"+Integer.toHexString( randomColor.getRGB() & 0x00ffffff );
  }


  private void updateSend(String channel,List<Attachment> attachments,List<LayoutBlock> layoutBlocks, String ts) {
    try {
      Slack.getInstance()
          .methods(slackToken)
          .chatUpdate(update ->
              update.channel(channel)
                  .attachments(attachments)
                  .blocks(layoutBlocks)
                  .ts(ts)
          );
    }catch (Exception e) {

    }
  }

  @Override
  public void send(String channel, String text) {
    List<Attachment> attachments = new ArrayList<>();
    List<LayoutBlock> layoutBlocks = new ArrayList<>();
    this.send(channel,attachments,layoutBlocks,text);
  }

  private String send(String channel,List<LayoutBlock> layoutBlocks) {
    List<Attachment> attachments = new ArrayList<>();
    return this.send(channel,attachments,layoutBlocks,"");
  }

  private String send(String channel, List<Attachment> attachments,List<LayoutBlock> layoutBlocks, String text){

    ChatPostMessageResponse chatPostMessageResponse = new ChatPostMessageResponse();

    try {
      chatPostMessageResponse =  Slack.getInstance()
          .methods(slackToken)
          .chatPostMessage(req ->
              req.channel(channel)
                  .blocks(layoutBlocks)
                  .attachments(attachments)
                  .text(text)
              .iconEmoji(":sushi:")
          );
    } catch (Exception e) {

    }

    return chatPostMessageResponse.getTs();
  }

  private void initVoteData(String channel) {

  }

}
