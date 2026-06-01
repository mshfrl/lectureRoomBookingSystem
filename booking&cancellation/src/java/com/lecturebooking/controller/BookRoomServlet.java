package com.lecturebooking.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import com.lecturebooking.dao.BookingDAO;

@WebServlet("/BookRoomServlet")
public class BookRoomServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        // 1. Get the session 
        HttpSession session = request.getSession();

        // 2. Fetch logged-in user's name from session 
        String applicantName = (String) session.getAttribute("activeUser"); 
        
        // Security check: if not logged in, kick back to login page 
        if (applicantName == null) {
            response.sendRedirect("login.jsp"); 
            return;
        }

        // 3. Grab details from HTML form 
        String roomName = request.getParameter("roomName"); 
        String location = request.getParameter("location"); 
        String bookingDate = request.getParameter("bookingDate"); 
        String bookingTime = request.getParameter("bookingTime"); 
        String purpose = request.getParameter("purpose");
        String supportingLecturer = request.getParameter("supportingLecturer"); 
        
        // 4. SMART STATUS ROUTING: 
        // If a supporting lecturer is chosen, it's a student booking = "Pending Lecturer"
        // If no lecturer is chosen (or it's empty), a lecturer is booking directly = "Pending Admin"
        String status;
        if (supportingLecturer != null && !supportingLecturer.trim().isEmpty()) {
            status = "Pending Lecturer";
        } else {
            status = "Pending Admin";
        }
        
        // 5. Send to database (Removed duplicate DAO instantiation)
        BookingDAO dao = new BookingDAO(); 
        try {
    // Attempt to insert the booking into the database
    boolean success = dao.insertBooking(applicantName, roomName, location, status, bookingDate, bookingTime, purpose);
    
    if (success) {
        // If it worked, redirect the student back to their dashboard 
        response.sendRedirect("StudentServlet"); 
    } else {
        // If it failed but didn't throw an error, send them back with a message
        request.setAttribute("errorMessage", "Booking failed. Please try again.");
        request.getRequestDispatcher("DashboardStudent.jsp").forward(request, response);
    }

} catch (Exception e) {
   
    e.printStackTrace(); 
    
    // Let user know something went wrong on the server side
    request.setAttribute("errorMessage", "A database error occurred: " + e.getMessage());
    request.getRequestDispatcher("DashboardStudent.jsp").forward(request, response);
}
    }
}
    