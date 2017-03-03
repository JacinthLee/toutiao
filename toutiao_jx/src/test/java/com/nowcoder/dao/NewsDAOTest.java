package com.nowcoder.dao;

import com.nowcoder.ToutiaoApplication;
import com.nowcoder.model.News;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class NewsDAOTest {

  @Autowired
  private NewsDAO newsDAO;

  @Test
  public void testSelectByIdAndOffset() {
    List<News> newses = newsDAO.selectByUserIdAndOffset(2, 0, 1);
    Assert.assertEquals(1, newses.size());
    newses = newsDAO.selectByUserIdAndOffset(0, 2, 8);
    Assert.assertEquals(8, newses.size());

  }
}