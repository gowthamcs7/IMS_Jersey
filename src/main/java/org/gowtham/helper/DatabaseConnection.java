package org.gowtham.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL= "jdbc:mysql://localhost:3306/invapp?serverTimezone=Asia/Kolkata";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";
    private static Connection connection;


    public static Connection getConnection(){
        try{
            if(connection==null || connection.isClosed()){
                synchronized (DatabaseConnection.class){
                    if(connection==null || connection.isClosed()){
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        connection = DriverManager.getConnection(URL,USERNAME,PASSWORD);
                    }
                }
            }
            if(connection==null){
                System.out.println("Connection to Database is failed");
            }
        }



        catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}
