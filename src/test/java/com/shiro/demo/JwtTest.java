package com.shiro.demo;

import com.shiro.demo.util.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class JwtTest {
    @Autowired
    private JwtUtil jwtUtil;

    @Test
    public void test(){
        log.info(jwtUtil.generateToken("wcf"));
    }
    @Test
    public void test2(){
        log.info(jwtUtil.getUsername("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3Y2YiLCJleHAiOjE1NzM0NDMxMDksImlhdCI6MTU3MjgzODMwOX0.djgUuwcGcAgD3Ayy1SMgjOJan7pz4oH5VWyhWeO3VPg"));
    }
    @Test
    public void test3(){
        log.info(jwtUtil.getTokenSi("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3Y2YiLCJleHAiOjE1NzM0NDMxMDksImlhdCI6MTU3MjgzODMwOX0.djgUuwcGcAgD3Ayy1SMgjOJan7pz4oH5VWyhWeO3VPg"));
    }
    @Test
    public void test4(){
        log.info(jwtUtil.getTokenHead("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ3Y2YiLCJleHAiOjE1NzM0NDMxMDksImlhdCI6MTU3MjgzODMwOX0.djgUuwcGcAgD3Ayy1SMgjOJan7pz4oH5VWyhWeO3VPg").toString());
    }
}
