package com.slack.food.repository;


import com.slack.food.domain.Category;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category,Long> {
  List<Category> findAllByChannel(String channel);
  Optional<Category> findBySeq(Long seq);
  void deleteByChannel(String channel);
  Optional<Category> findFirstByChannelOrderByGoodDesc(String channel);
}
