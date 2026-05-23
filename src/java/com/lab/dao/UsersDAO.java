/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lab.dao;

import com.lab.model.Users;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;


/**
 *
 * @author Asus
 */
public class UsersDAO {
     private String jdbcURL = "jdbc:mysql://localhost:3307/lectureRoomDatabase";
    private String jdbcUsername = "root";
    private String jdbcPassword = "";

    // Method untuk mendapatkan sambungan Database
    protected Connection getConnection() {
        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(jdbcURL, jdbcUsername, jdbcPassword);
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return connection;
    }
    
    public String authenticateUser(String username, String password) {
    String role = null;
    // Assuming you have your DB connection code here...
    String query = "SELECT role FROM users WHERE username = ? AND password = ?";
    
    try (PreparedStatement pst = getConnection().prepareStatement(query)) {
        pst.setString(1, username);
        pst.setString(2, password);
        ResultSet rs = pst.executeQuery();
        
        if (rs.next()) {
            role = rs.getString("role"); // e.g., "admin" or "student"
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return role; // Returns null if login fails
}
}
