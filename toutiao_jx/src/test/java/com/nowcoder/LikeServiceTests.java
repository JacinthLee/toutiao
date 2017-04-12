package com.nowcoder;

import com.nowcoder.service.LikeService;
import org.junit.*;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Created by Jacinth on 2017/4/11.
 * 单元测试
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = ToutiaoApplication.class)
public class LikeServiceTests {
    @Autowired
    LikeService likeService;  //验证数据

    @Test//2.跑一个测试用例
    public void testLike() {
        likeService.like(123, 1, 1);
        Assert.assertEquals(1, likeService.getLikeStatus(123, 1, 1));
    }

    @Test
    public void testDislike() {
        likeService.disLike(123, 1, 1);
        Assert.assertEquals(-1, likeService.getLikeStatus(123, 1, 1));
    }

    //异常测试。正常情况下也会抛异常
    @Test(expected = IllegalArgumentException.class)
    public void testException() {
        throw new IllegalArgumentException("异常");
    }

    @Before//1.初始化数据
    public void setUp() {
        System.out.println("setUp");
    }

    @After//3.清理数据
    public void tearDown() {
        System.out.println("tearDown");
    }

    @BeforeClass//所有测试用例之前，只跑一次
    public static void beforeClass() {  //全局，所以是static关键词，静态
        System.out.println("beforeClass");
    }

    @AfterClass//所有测试用例之前，只跑一次
    public static void afterClass() {
        System.out.println("afterClass");
    }

}
