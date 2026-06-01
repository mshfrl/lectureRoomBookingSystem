package com.lecturebooking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import com.lecturebooking.dao.BookingDAO;
import com.lecturebooking.model.Room;
import com.lecturebooking.model.Booking;

@WebServlet("/StudentServlet")
public class StudentServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    // function dapat session username student yang aktif
    private String getLoggedUser(HttpSession session) {
        String user = (String) session.getAttribute("username"); 
        if (user == null || user.trim().isEmpty()) {
            user = (String) session.getAttribute("studentUsername");
        }
        
        // for testing
        if (user == null || user.trim().isEmpty()) {
            user = "ali_student"; 
        }
        return user.trim();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        BookingDAO dao = new BookingDAO();
        String currentStudent = getLoggedUser(request.getSession());

        try {
            // 1. Ambil senarai bilik kuliah yang tersedia (untuk paparan Grid)
            String searchQuery = request.getParameter("searchQuery");
            List<Room> roomsList;
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                roomsList = dao.searchRooms(searchQuery.trim());
            } else {
                roomsList = dao.getAllRooms();
            }
            request.setAttribute("listAvailableRooms", roomsList);

            // 2. Ambil booking khusus untuk student ini (Supaya seksyen 'My Booking' sentiasa kemas kini)
            List<Booking> studentBookings = dao.getBookingsByStudent(currentStudent);
            request.setAttribute("listStudentBookings", studentBookings);

        } catch (Exception e) {
    System.err.println("Error loading the student data table in StudentServlet:");
    e.printStackTrace();
}

        // Hantar data ke halaman JSP student dashboard
        request.getRequestDispatcher("DashboardStudent.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        BookingDAO bookingDAO = new BookingDAO();
        String currentStudent = getLoggedUser(request.getSession());

        // Ambil ID bilik dari hidden input modal borang tempahan
        String roomId = request.getParameter("roomId");
        
        if (roomId != null && !roomId.trim().isEmpty()) {
            //  nama parameter dengan <select name="supportingLecturer"> di JSP
            String lecturerUsername = request.getParameter("supportingLecturer"); 
            String date = request.getParameter("date");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String purpose = request.getParameter("purpose");

            // Seragam format masa (HH:mm:ss) untuk data MySQL
            if (startTime != null && startTime.length() == 5) startTime += ":00";
            if (endTime != null && endTime.length() == 5) endTime += ":00";

            // Panggil logik DAO untuk memasukkan data permohonan baru
            boolean isBooked = bookingDAO.insertBooking(roomId, currentStudent, lecturerUsername, date, startTime, endTime, purpose);

            if (isBooked) {
                System.out.println("Student (" + currentStudent + ") has successfully submitted the room booking request!\"");
            } else {
                System.out.println("Failed to process student booking request.");
            }
        }

        // Selepas tamat proses simpan, refresh semula ke StudentServlet 
        response.sendRedirect("StudentServlet");
    }
}