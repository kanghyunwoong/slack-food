package com.slack.food.domain;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import org.springframework.stereotype.Component;

@Component
public class Step{

  private ScheduledExecutorService scheduledExecutorService;
  private TimeUnit timeUnit;

  public Step() {
    this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    this.timeUnit = TimeUnit.SECONDS;
  }

  public void start(Runnable runnable,Long interval) {
   scheduledExecutorService.schedule(runnable ,interval, timeUnit);
  }

  public void setTimeUnit(TimeUnit timeUnit) {
    this.timeUnit = timeUnit;
    };

}
