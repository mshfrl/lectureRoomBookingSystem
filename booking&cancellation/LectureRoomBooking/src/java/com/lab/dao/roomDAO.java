/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lab.dao;

import com.lab.model.room;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 *
 * @author Asus
 */
public class roomDAO {

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
    
    //retrieve all room from database
    public ArrayList<room> selectAllRooms(){
        ArrayList<room> rooms = new ArrayList<>();
        
        String select_all_rooms = "select * from lectureRooms";
        
        try (Connection connection = getConnection(); PreparedStatement preparedStatement = connection.prepareStatement(select_all_rooms);){
            System.out.println(preparedStatement);
            ResultSet rs = preparedStatement.executeQuery();
            
            while (rs.next()){
                String roomID = rs.getString("roomID");
                String roomName = rs.getString("roomName");
                int capacity = rs.getInt("capacity");
                String location = rs.getString("location");
                Boolean availability = rs.getBoolean("availability");
                
                rooms.add(new room(roomID, roomName, capacity, location, availability));
            }
            
        }catch (SQLException e){
            e.printStackTrace();
        }
        return rooms;
    }
    
    
    public ArrayList<room> searchRooms(String keyword) {
        ArrayList<room> rooms = new ArrayList<>();
        // This query searches for matches in the ID, Name, OR Location
        String search_rooms = "select * from lectureRooms where roomID like ? or roomName like ? or location like ?";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(search_rooms);) {
            
            
            String searchPattern = "%" + keyword + "%";
            preparedStatement.setString(1, searchPattern);
            preparedStatement.setString(2, searchPattern);
            preparedStatement.setString(3, searchPattern);

            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
                rooms.add(new room(rs.getString("roomID"), rs.getString("roomName"), rs.getInt("capacity"), rs.getString("location"), rs.getBoolean("availability")));
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return rooms;
    }
    
    
    
    // --- 2. CREATE (Add new room) ---
    public void insertRoom(room r) {
        String insert_room = "insert into lectureRooms (roomID, roomName, capacity, location, availability) VALUES (?, ?, ?, ?, ?)";
        try (Connection connection = getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(insert_room)) {
            
            preparedStatement.setString(1, r.getRoomID());
            preparedStatement.setString(2, r.getRoomName());
            preparedStatement.setInt(3, r.getCapacity());
            preparedStatement.setString(4, r.getLocation());
            preparedStatement.setBoolean(5, r.getAvailability());
            preparedStatement.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 3. UPDATE (Edit existing room) ---
    public void updateRoom(room r) {
        String update_room = "update lectureRooms set roomName=?, capacity=?, location=?, availability=? WHERE roomID=?";
        try (Connection connection = getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(update_room)) {
            
            preparedStatement.setString(1, r.getRoomName());
            preparedStatement.setInt(2, r.getCapacity());
            preparedStatement.setString(3, r.getLocation());
            preparedStatement.setBoolean(4, r.getAvailability());
            preparedStatement.setString(5, r.getRoomID());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // --- 4. DELETE (Remove room) ---
    public void deleteRoom(String roomID) {
        String delete_room = "delete from lectureRooms where roomID=?";
        try (Connection connection = getConnection(); 
             PreparedStatement preparedStatement = connection.prepareStatement(delete_room)) {
            
            preparedStatement.setString(1, roomID);
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
}
