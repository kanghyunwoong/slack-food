import com.slack.food.SlackBotApplication;
import com.slack.food.config.CustomScheduler;
import com.slack.food.service.KakaoMapService;
import com.slack.food.service.SlackBotService;
import com.slack.food.web.dto.Event;
import java.util.Timer;
import java.util.TimerTask;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = SlackBotApplication.class)
public class ScheduleTestController {

  @Autowired
  private CustomScheduler customScheduler;

  private SlackBotService kakaoMapService;

  private Event event;


  @Before
  public void setup() {
    event = Event.builder()
        .type("app_mention")
        .channel("C01626NHE13")
        .text("<@U0169PM0RD2> 점")
        .user("U0169P1KH0C")
        .build();
  }

  @Test
  public void 스케쥴테스트() {
    Timer jobScheduler = new Timer();
    jobScheduler.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        System.out.println("test1");
      }
    }, 1000, 3000);

    jobScheduler.scheduleAtFixedRate(new TimerTask() {
      @Override
      public void run() {
        System.out.println("test2");
      }
    },2000,3000);

  }

  @Test
  public void 스케쥴테스트2() {
    System.out.println("fdafda");
  }

  @Scheduled(fixedDelay = 3000)
  public void test() {
    System.out.println("테스트이빈다.");
  }


}
