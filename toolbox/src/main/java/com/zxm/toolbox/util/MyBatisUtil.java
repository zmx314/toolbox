package com.zxm.toolbox.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public class MyBatisUtil {
    //sqlSessionFactory --> sqlSession
    static SqlSessionFactory sqlSessionFactory = null;

    static {
        try {
            /**
             * 使用Mybatis第一步 ：
             * 读取配置文件mybatis-config.xml；
             * 根据配置文件构建SqlSessionFactory；
             */
            InputStream inputStream = Resources.getResourceAsStream("mybatis-config.xml");
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //通过SqlSessionFactory创建SqlSession
    //SqlSession提供了在数据库执行SQL命令所需的所有方法。
    public static SqlSession getSqlSession() {
        return sqlSessionFactory.openSession();
    }
}
