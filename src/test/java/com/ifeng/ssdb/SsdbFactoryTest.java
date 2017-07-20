package com.ifeng.ssdb;

import com.hyd.ssdb.SsdbClient;
import com.hyd.ssdb.SsdbClientFactory;
import org.junit.Test;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by lilg1 on 2017/7/19.
 */
public class SsdbFactoryTest {

//    @Test
//    public void testFactory(){
//        EnvTest.setEnv();
//        System.out.println("test"+SsdbClientFactory.getEnviroment());
//        SsdbClient ssdbClient = SsdbClientFactory.getSsdbClient();
//        String result = ssdbClient.get("1111");
//        System.out.println(result);
//    }
//
//    @Test
//    public void testEnv(){
//        System.out.println(SsdbClientFactory.getEnvironment());
//    }
    @Test
    public void testFactory(){
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("classpath*:application-context.xml");

    }
}
