package com.slack.food.repository;

import com.slack.food.domain.Location;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepositoy extends JpaRepository<Location,Long> {
  List<Location> findAllByChannel(String channel);
  Optional<Location> findFirstByChannelOrderByGoodDesc(String channel);
  Optional<Location> findBySeq(Long seq);
  void deleteByChannel(String channel);
  List<Location> findAllByChannelAndCateDepth2(String channel, String cate);

}
