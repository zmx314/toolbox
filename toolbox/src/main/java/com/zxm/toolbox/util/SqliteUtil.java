package com.zxm.toolbox.util;

import com.zxm.toolbox.resources.Resources;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;

public class SqliteUtil {
    private static final Logger LOGGER = LogManager.getLogger(SqliteUtil.class);
    public static Connection getConnection(){
        Connection c = null;
        try {
            Class.forName("org.sqlite.JDBC");
            c = DriverManager.getConnection("jdbc:sqlite:" + Resources.SQLITE_DATA_FILE.getAbsolutePath());
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            LOGGER.error(e);
        }
//        LOGGER.info("Opened database successfully");
        return c;
    }

    public static void createTable(Connection c, String sql){
        LOGGER.info(Thread.currentThread().getStackTrace()[1].getMethodName());
        LOGGER.info(sql);
        try {
            Statement stmt = c.createStatement();
            stmt.executeUpdate(sql);
            stmt.close();
            c.close();
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.error(e);
        }
        System.out.println("Table created successfully");
    }

}
