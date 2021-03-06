package com.flow.origo;

import java.sql.*;
import java.util.List;

public class JDBConnector {
    // JDBC driver name and database URL
    private static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost";
    private HTMLParser htmlParser = new HTMLParser();

    public void jdbcHandler(List<Article> objectList, String tag) {
        Connection conn = null;
        Statement stmt = null;
        try {
            //STEP 2: Register JDBC driver
            Class.forName(JDBC_DRIVER);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        //STEP 3: Open a connection
        try {
            System.out.println("Connecting to a selected database.");
            conn = DriverManager.getConnection(DB_URL + "/origoDB", "root", "toor");
            System.out.println("Connected database successfully.");

            //STEP 4: Execute a query
            System.out.println("Creating table in given database.");
            stmt = conn.createStatement();


            String deleteTable = "DROP TABLE IF EXISTS `" + htmlParser.removeAccents(tag) + "`;";
            stmt.executeUpdate(deleteTable);

            String createTable = "CREATE TABLE `" + htmlParser.removeAccents(tag) + "` (" +
                    "  `title` MEDIUMTEXT NULL," +
                    "  `author` VARCHAR(255) NULL," +
                    "  `date` DATETIME NULL," +
                    "  `additional_tags` MEDIUMTEXT NULL," +
                    "  `content` LONGTEXT NULL);";

            stmt.executeUpdate(createTable);
            System.out.println("Created table in given database.");

        } catch (SQLException e) {
            e.printStackTrace();
        }

        String createRow = "";

        for (Article object : objectList) {
            createRow = "INSERT INTO " + htmlParser.removeAccents(tag) + " VALUES (" +
                    "'" + object.getTitle() + "', " +
                    "'" + object.getAuthor() + "', " +
                    "'" + object.getDate() + "', " +
                    "'" + object.getAdditionalTags() + "', " +
                    "'" + object.getContent() + "');";
            try {
                stmt.executeUpdate(createRow);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Created rows in given table.");

        try {
            if (stmt != null)
                conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }
        try {
            if (conn != null)
                conn.close();
        } catch (SQLException se) {
            se.printStackTrace();
        }

    }
}
