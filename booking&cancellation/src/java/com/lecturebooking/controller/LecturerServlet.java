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

@WebServlet("/LecturerServlet")
public class LecturerServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private String getLoggedUser(HttpSession session) {
        String user = (String) session.getAttribute("username"); 
        if (user == null || user.trim().isEmpty()) {
            user = (String) session.getAttribute("lecturerUsername");
        }
        
        // test purpose 
        if (user == null || user.trim().isEmpty()) {
            user = "khairul_anuar"; 
        }
        return user.trim();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        BookingDAO dao = new BookingDAO();
        String currentLecturer = getLoggedUser(request.getSession());

        try {
            // 1. ambik request Pelajar yang status 'Pending Lecturer'
            List<Booking> pendingRequests = dao.getPendingRequestsForLecturer(currentLecturer);
            request.setAttribute("listPendingRequests", pendingRequests);

            // 2. search
            String searchQuery = request.getParameter("searchQuery");
            List<Room> roomsList;
            if (searchQuery != null && !searchQuery.trim().isEmpty()) {
                roomsList = dao.searchRooms(searchQuery.trim());
            } else {
                roomsList = dao.getAllRooms();
            }
            request.setAttribute("listAvailableRooms", roomsList);

            // 3. Ambil Rekod Tempahan Peribadi Pensyarah Ini
            List<Booking> lecturerBookings = dao.getBookingsByLecturer(currentLecturer);
            request.setAttribute("listLecturerBookings", lecturerBookings);

        } catch (Exception e) {
            System.err.println("❌ Ralat ketika memuatkan data dashboard di LecturerServlet:");
            e.printStackTrace();
        }

        request.getRequestDispatcher("DashboardLecturer.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        BookingDAO bookingDAO = new BookingDAO();
        String currentLecturer = getLoggedUser(request.getSession());
        
        // ambik parameter action daripada form JSP
        String action = request.getParameter("action");

        
        // process of approve / reject request 
        
        if ("approve".equals(action) || "reject".equals(action)) {
            String studentUsername = request.getParameter("studentUsername");
            String roomName = request.getParameter("roomName");
            String bookingDate = request.getParameter("bookingDate");
            String bookingTime = request.getParameter("bookingTime");

            // update new status 
            String targetStatus = "approve".equals(action) ? "Pending Admin" : "Rejected";

            boolean isUpdated = bookingDAO.updateBookingStatus(studentUsername, roomName, bookingDate, bookingTime, targetStatus);
            
            if(isUpdated) {
                System.out.println("✅ Berjaya kemas kini status kepada: " + targetStatus);
            } else {
                System.out.println("❌ Gagal mengemas kini status. Sila semak padanan data.");
            }

            // Refresh halaman dashboard untuk papar data paling terkini
            response.sendRedirect("LecturerServlet");
            return; 
        }
        
        
        //  lecturer made request book room
        String roomId = request.getParameter("roomId");
        if (roomId != null && !roomId.trim().isEmpty()) {
            String date = request.getParameter("date");
            String startTime = request.getParameter("startTime");
            String endTime = request.getParameter("endTime");
            String purpose = request.getParameter("purpose");
            
            //  format masa ke HH:mm:ss untuk database 
            if (startTime != null && startTime.length() == 5) startTime += ":00";
            if (endTime != null && endTime.length() == 5) endTime += ":00";

            // Masukkan rekod baru ke dalam database 
            boolean isBooked = bookingDAO.insertBooking(roomId, currentLecturer, currentLecturer, date, startTime, endTime, purpose);
            
            if (isBooked) {
                System.out.println("successfully submitted the room booking request!");
            } else {
                System.out.println(" Failed to process lecturer booking request.");
            }
            
            response.sendRedirect("LecturerServlet");
            return;
        }
    }
}