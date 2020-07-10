import static com.slack.api.model.block.Blocks.actions;
import static com.slack.api.model.block.Blocks.divider;
import static com.slack.api.model.block.Blocks.section;
import static com.slack.api.model.block.composition.BlockCompositions.markdownText;
import static com.slack.api.model.block.composition.BlockCompositions.plainText;
import static com.slack.api.model.block.element.BlockElements.asElements;
import static com.slack.api.model.block.element.BlockElements.button;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slack.api.Slack;
import com.slack.api.model.block.LayoutBlock;
import com.slack.food.SlackBotApplication;
import com.slack.food.config.SlackBotReq;
import com.slack.food.domain.Location;
import com.slack.food.repository.LocationRepositoy;
import com.slack.food.service.SlackBotService;
import com.slack.food.util.CommonUtil;
import com.slack.food.web.dto.Event;
import com.slack.food.web.dto.KakaoReq;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackBotApplication.class)
public class SlackBotTestController {

  private  Event event;
  private static List<String> list;

  @Autowired
  private RestTemplate kakaoRestTemplate;

  @Autowired
  private RestTemplate restTemplate;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private LocationRepositoy locationRepositoy;

  @Autowired
  private SlackBotService slackBotService;

  @Before
  public void setup() {
    list = Arrays.asList("점심","저녁","회식");
    event = Event.builder()
        .type("app_mention")
        .channel("C01626NHE13")
        .text("<@U0169PM0RD2> 점")
        .user("U0169P1KH0C")
        .build();
  }

  @Test
  public void testttt() {
  }

  @Test
  public void 에에에에() {
    StringBuilder builder = new StringBuilder();
    builder.append("https://dapi.kakao.com/v2/local/search/address.json?")
        .append("query=서울특별시 성동구 성수일로6길 33");

    ResponseEntity<JsonNode> response = kakaoRestTemplate.getForEntity(builder.toString(), JsonNode.class);
    JsonNode result = response.getBody().get("documents");

    if(result.isEmpty()) {
      //Exception
    }

    String x = result.get(0).get("x").asText();
    String y = result.get(0).get("y").asText();


    try {
      CommonUtil.convertDto(response.getBody().get("documents").toString(),KakaoReq[].class);
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void 결과세팅() {
    List<LayoutBlock> layoutBlocks = new ArrayList<>();
    layoutBlocks.add(section(section -> section.text(plainText("현재 위치 세팅",true))));
    layoutBlocks.add(divider());
    layoutBlocks.add(
        actions(actions -> actions
            .elements(asElements(
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("승인")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("거부")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("일식")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("ㅇ2")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("ㅂㅈ")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("42")))
                )
            ))
        )
    );

    send(event.getChannel(),layoutBlocks,null);
  }



  @Test
  public void ㄹ아아아아() {
    String result = "";
    int bStart = 0;
    for(int i = 0; i < 5; i ++) {
      if(bStart > i) {
        result += "★";
      }else {
        result += "☆";
      }
    }
    System.out.println("result : "+result);
  }

  @Test
  public void 에에엥() {
    List<LayoutBlock> layoutBlocks = new ArrayList<>();
    layoutBlocks.add(section(section -> section.text(markdownText("새로운 배송팁이 등록되었습니다."))));
    layoutBlocks.add(divider());
    layoutBlocks.add(
        actions(actions -> actions
            .elements(asElements(
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("승인")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("거부")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("일식")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("ㅇ2")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("ㅂㅈ")))
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("42")))
                )
            ))
        )
    );

    send(event.getChannel(),layoutBlocks,null);

  }

  private final static String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_9_2) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/33.0.1750.152 Safari/537.36";


  private static SlackBotReq slackBotReq;

  @Test
  public void 테테테테테테테테() {
    List<LayoutBlock> layoutBlocks = slackBotReq.sectionTitle("fdas",false).end();

    System.out.println(layoutBlocks);

  }


  @Test
  public void 테스트트트() {
    String url = "https://place.map.kakao.com/83147987";

    String last = url.substring(url.lastIndexOf("/"));
    String apiUrl = "https://place.map.kakao.com/m/main/v/"+last;


    ResponseEntity<JsonNode> response = kakaoRestTemplate.getForEntity(apiUrl, JsonNode.class);
    JsonNode jsonNode = response.getBody();

    String mainImage = jsonNode.get("basicInfo").get("mainphotourl").asText();
    Double scoreSum = jsonNode.get("comment").get("scoresum").asDouble();
    Double scoreCnt = jsonNode.get("comment").get("scorecnt").asDouble();

    DecimalFormat decimalFormat = new DecimalFormat(".#");
    String scoreAvg = decimalFormat.format(scoreSum/scoreCnt);

    System.out.println("scoreAvgg"+scoreAvg);
  }

  @Test
  public void 첫번쨰() {
    ScheduledExecutorService sc = Executors.newSingleThreadScheduledExecutor();
    Future f1 = sc.schedule(()->{ System.out.println("테스트1"); },100,TimeUnit.SECONDS);


    ScheduledFuture a =  sc.schedule(()-> {
      System.out.println("스텝1");
      return "a";
    },1L, TimeUnit.SECONDS);
    ScheduledFuture b =  sc.schedule(()-> {
      System.out.println("스텝2");
    },3L, TimeUnit.SECONDS);

    ScheduledFuture c =  sc.schedule(()-> {
      System.out.println("스텝3");
    },1L, TimeUnit.SECONDS);


    try {
      System.out.println("fdsfaf : "+a.get());

    } catch (InterruptedException e) {
      e.printStackTrace();
    } catch (ExecutionException e) {
      e.printStackTrace();
    }

  }


  private Runnable step1() {
    return ()->{
      System.out.println("STEP!");
    };
  }
  private Runnable step2() {
    return ()-> {
      System.out.println("STEP!!");
    };
  }

  private Runnable step3() {
    return ()-> {
      System.out.println("STEP!!!");
    };
  }


  @Test
  public void 카카오테스트() throws JsonProcessingException {


    String query = "https://dapi.kakao.com/v2/local/search/category.json?radius=20000&category_group_code=FD6";
    HttpHeaders headers = new HttpHeaders();
    HttpEntity<?> entity = new HttpEntity<>(headers);
/*

    List<Kakao> kakaoList = new ArrayList<>();
    ResponseEntity<Map> response = kakaoRestTemplate.exchange(query,HttpMethod.GET,entity,Map.class);
*/

    ResponseEntity<JsonNode> response = kakaoRestTemplate.getForEntity(query, JsonNode.class);

    KakaoReq[] d = objectMapper.readValue(response.getBody().get("documents").toString(),KakaoReq[].class);


    System.out.println("fdsafdsa");
  }

  private void sendd(Event event) {
    List<LayoutBlock> layoutBlocks = new ArrayList<>();
    layoutBlocks.add(section(section -> section.text(markdownText("새로운 배송팁이 등록되었습니다."))));
    layoutBlocks.add(divider());
    layoutBlocks.add(
        actions(actions -> actions
            .elements(asElements(
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("승인")))
                    .style("primary")
                    .actionId("action_approve")
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("거부")))
                    .style("danger")
                    .actionId("action_reject")
                )
            ))
        )
    );

    send(event.getChannel(),layoutBlocks,null);
  }

  @Test
  public void 메이크블록() {

    List<LayoutBlock> layoutBlocks = new ArrayList<>();
    layoutBlocks.add(section(section -> section.text(markdownText("테스트코드."))));
    layoutBlocks.add(divider());
    layoutBlocks.add(
        actions(actions -> actions
            .elements(asElements(
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("한식")))
                    .style("primary")
                    .actionId("action_approve")
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("중식")))
                    .style("danger")
                    .actionId("action_reject")
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("일식")))
                    .style("danger")
                    .actionId("action_reject")
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("ㅇ2")))
                    .style("danger")
                    .actionId("action_reject")
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("ㅂㅈ")))
                    .style("danger")
                    .actionId("action_reject")
                ),
                button(b -> b.text(plainText(pt -> pt.emoji(true).text("42")))
                    .style("danger")
                    .actionId("action_reject")
                )
            ))
        )
    );

    send(event.getChannel(),layoutBlocks,null);
  }

  private void send(String channel, List<LayoutBlock> layoutBlocks, String text){
    try {
      Slack.getInstance()
          .methods("xoxb-1207811873123-1213803025444-SanHL5nbSoPQvH74OtfbI2jR")
          .chatPostMessage(req ->
              req.channel(channel)
                  .blocks(layoutBlocks)
                  .text(StringUtils.isBlank(text) ? "" : text)
          );
    } catch (Exception e) {

    }
  }


}
