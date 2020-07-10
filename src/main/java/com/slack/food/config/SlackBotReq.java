package com.slack.food.config;


import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;

import com.slack.api.model.Attachment;
import com.slack.api.model.block.LayoutBlock;
import com.slack.api.model.block.SectionBlock;
import com.slack.api.model.block.composition.TextObject;
import com.slack.api.model.block.element.ImageElement;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.StringUtils;

public class SlackBotReq {

  private List<LayoutBlock> layoutBlockList = new ArrayList<>();


  public SlackBotReq sectionTitle(String title, boolean emoji) {
    TextObject textObject = emoji ? plainText(title,true) : markdownText(title);

    this.layoutBlockList.add(SectionBlock
        .builder()
        .text(textObject)
        .build());

    return this;
  }

  public SlackBotReq sectionImage(String title,String emoji, String imageUrl, String imageAlt) {

    TextObject textObject = StringUtils.isEmpty(emoji) ? markdownText(title) : plainText(title,true);

    this.layoutBlockList.add(SectionBlock
        .builder()
        .text(textObject)
        .accessory(ImageElement.builder().imageUrl(imageUrl).altText(imageAlt).build())
        .build()
    );

    return this;
  }

  public List<LayoutBlock> end() {
    return this.layoutBlockList;
  }



  public static Attachment makeAttachment() {
    return Attachment.builder().build();
  }

}
