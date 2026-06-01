package com.lecturebooking.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.lecturebooking.model.Room;
import com.lecturebooking.model.Booking;
import com.lecturebooking.model.Lecturer;

public class BookingDAO {

    // --- DATABASE CONNECTION CONFIGURATION ---
    private Connection getConnection() throws Exception {
        String jdbcURL = "jdbc:mysql://localhost:3306/lectureRoom?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC";
        String dbUser = "root";      
        String dbPassword = "";      
        
        Class.forName("com.mysql.cj.jdbc.Driver");
        return DriverManager.getConnection(jdbcURL, dbUser, dbPassword);
    }

    // --- FEATURE 1: FETCH ALL ROOMS FOR THE DASHBOARD GRID ---
    public List<Room> getAllRooms() {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Room room = new Room();
                room.setRoomId(rs.getString("room_id"));
                room.setRoomName(rs.getString("room_name"));
                room.setLocation(rs.getString("location"));
                room.setCapacity(rs.getInt("capacity"));
                rooms.add(room);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

    // --- FEATURE 2: LIVE FILTER SEARCH BAR ---
    public List<Room> searchRooms(String query) {
        List<Room> rooms = new ArrayList<>();
        String sql = "SELECT * FROM rooms WHERE room_id LIKE ? OR room_name LIKE ? OR location LIKE ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            String searchPattern = "%" + query + "%";
            ps.setString(1, searchPattern);
            ps.setString(2, searchPattern);
            ps.setString(3, searchPattern);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Room room = new Room();
                    room.setRoomId(rs.getString("room_id"));
                    room.setRoomName(rs.getString("room_name"));
                    room.setLocation(rs.getString("location"));
                    room.setCapacity(rs.getInt("capacity"));
                    rooms.add(room);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rooms;
    }

    // --- FEATURE 3: GET BOOKINGS SUBMITTED BY STUDENT ---
    public List<Booking> getBookingsByStudent(String studentUsername) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, r.room_name, r.location, u.full_name AS lecturer_name " +
                     "FROM bookings b " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "LEFT JOIN users u ON b.lecturer_username = u.username " +
                     "WHERE b.student_username = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, studentUsername);
            
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setStudentName(rs.getString("student_username")); 
                    booking.setRoomName(rs.getString("room_name"));
                    booking.setLocation(rs.getString("location"));
                    booking.setStatus(rs.getString("status")); 
                    booking.setBookingDate(rs.getString("booking_date"));
                    booking.setBookingTime(rs.getString("start_time") + " - " + rs.getString("end_time")); 
                    booking.setPurpose(rs.getString("purpose"));
                    booking.setSupportingLecturer(rs.getString("lecturer_name")); 
                    
                    bookings.add(booking);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // --- FEATURE 4: INSERT BOOKING FROM STUDENT OR LECTURER ---
    public boolean insertBooking(String roomId, String studentUsername, String lecturerUsername, 
                                 String date, String startTime, String endTime, String purpose) {
    
        String initialStatus = "Pending Lecturer";
        if (lecturerUsername == null || lecturerUsername.trim().isEmpty() || studentUsername.equals(lecturerUsername)) {
            initialStatus = "Pending Admin"; 
        }
        
        String sql = "INSERT INTO bookings (room_id, student_username, lecturer_username, booking_date, start_time, end_time, purpose, status) " +
                     "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                     
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, roomId);
            ps.setString(2, studentUsername); 
            
            if (lecturerUsername == null || lecturerUsername.trim().isEmpty() || studentUsername.equals(lecturerUsername)) {
                ps.setNull(3, java.sql.Types.VARCHAR);
            } else {
                ps.setString(3, lecturerUsername);
            }
            
            ps.setString(4, date);
            ps.setString(5, startTime);
            ps.setString(6, endTime);
            ps.setString(7, purpose);
            ps.setString(8, initialStatus);
            
            int rows = ps.executeUpdate();
            return rows > 0;
        } catch (Exception e) {
            System.err.println("❌ Error inside insertBooking DAO:");
            e.printStackTrace();
            return false;
        }
    }

    // --- FEATURE 5: UPDATE BOOKING STATUS VIA POST (BETUL & SELARAS) --- 
    public boolean updateBookingStatus(String studentUsername, String roomName, String bookingDate, String bookingTime, String targetStatus) {
        // Menggunakan subquery untuk menukar room_name dari UI kepada room_id di table bookings
        String sql = "UPDATE bookings SET status = ? WHERE student_username = ? " +
                     "AND room_id = (SELECT room_id FROM rooms WHERE room_name = ? LIMIT 1) " +
                     "AND booking_date = ? AND start_time = ?"; 
        
        // Memotong string "12:30:00 - 15:30:00" untuk mendapatkan "12:30:00" (start_time)
        String startTime = "00:00:00";
        if (bookingTime != null && bookingTime.contains("-")) {
            startTime = bookingTime.split("-")[0].trim();
        } else if (bookingTime != null) {
            startTime = bookingTime.trim();
        }
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, targetStatus); 
            ps.setString(2, studentUsername);
            ps.setString(3, roomName);
            ps.setString(4, bookingDate);
            ps.setString(5, startTime);
            
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.err.println("❌ Error inside updateBookingStatus DAO:");
            e.printStackTrace();
            return false;
        }
    }

    // --- FEATURE 6: GET PENDING REQUESTS WAITING FOR THIS LECTURER'S APPROVAL ---
    public List<Booking> getPendingRequestsForLecturer(String lecturerUsername) {
        List<Booking> bookings = new ArrayList<>();
        String sql = "SELECT b.*, r.room_name, r.location " +
                     "FROM bookings b " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "WHERE b.lecturer_username = ? AND b.status = 'Pending Lecturer'"; 
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, lecturerUsername);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking b = new Booking();
                    b.setStudentName(rs.getString("student_username")); 
                    b.setRoomName(rs.getString("room_name"));
                    b.setLocation(rs.getString("location"));
                    b.setBookingDate(rs.getString("booking_date"));
                    b.setBookingTime(rs.getString("start_time") + " - " + rs.getString("end_time"));
                    b.setPurpose(rs.getString("purpose"));
                    b.setStatus(rs.getString("status"));
                    b.setSupportingLecturer(lecturerUsername);
                    bookings.add(b);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bookings;
    }

    // --- FEATURE 7: GET BOOKINGS MADE PERSONALLY BY THE LECTURER ---
    public List<Booking> getBookingsByLecturer(String lecturerUsername) {
        List<Booking> list = new ArrayList<>();
        String sql = "SELECT b.*, r.room_name, r.location FROM bookings b " +
                     "JOIN rooms r ON b.room_id = r.room_id " +
                     "WHERE b.student_username = ? ORDER BY b.booking_date DESC";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            
            ps.setString(1, lecturerUsername);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Booking booking = new Booking();
                    booking.setRoomName(rs.getString("room_name"));
                    booking.setLocation(rs.getString("location"));
                    booking.setBookingDate(rs.getString("booking_date"));
                    booking.setBookingTime(rs.getString("start_time") + " - " + rs.getString("end_time"));
                    booking.setPurpose(rs.getString("purpose"));
                    booking.setStatus(rs.getString("status"));
                    list.add(booking);
                }
            }
        } catch (Exception e) {
            System.err.println("❌ Error inside getBookingsByLecturer DAO:");
            e.printStackTrace();
        }
        return list;
    }
  
    // --- FEATURE 8: LIST ALL LECTURERS FOR DROPDOWN MODAL --- 
    public List<Lecturer> getAllLecturers() {
        List<Lecturer> list = new ArrayList<>();
        String query = "SELECT username, full_name FROM users WHERE role = 'Lecturer' OR role = 'lecturer'";
        
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            
            while (rs.next()) {
                Lecturer lec = new Lecturer();
                lec.setUsername(rs.getString("username"));  
                lec.setFullName(rs.getString("full_name")); 
                list.add(lec);
            }
        } catch (Exception e) {
            System.out.println("Error fetching lecturers in BookingDAO: " + e.getMessage());
            e.printStackTrace();
        }
        return list;
    }
}