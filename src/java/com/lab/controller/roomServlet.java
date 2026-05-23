/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.lab.controller;

import com.lab.dao.roomDAO;
import com.lab.model.room;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;
import java.io.PrintWriter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 *
 * @author Asus
 */
@WebServlet("/roomServlet")
public class roomServlet extends HttpServlet {

    private roomDAO dao;

    @Override
    public void init() {
        dao = new roomDAO();
    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        try (PrintWriter out = response.getWriter()) {
            /* TODO output your page here. You may use following sample code. */
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Servlet roomServlet</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>Servlet roomServlet at " + request.getContextPath() + "</h1>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String action = request.getParameter("action");

        if (action == null) {
            response.sendRedirect("admin.jsp");
            return;
        }

        try {
            switch (action) {
                case "add":
                    addRoom(request, response);
                    break;
                case "edit":
                    editRoom(request, response);
                    break;
                case "delete":
                    deleteRoom(request, response);
                    break;
                default:
                    response.sendRedirect("admin.jsp");
                    break;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            // If there's an error, send back to admin page
            response.sendRedirect("admin.jsp");
        }
    }

    private void addRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomID = request.getParameter("roomID");
        String roomName = request.getParameter("roomName");
        int capacity = Integer.parseInt(request.getParameter("capacity"));
        String location = request.getParameter("location");
        boolean availability = Boolean.parseBoolean(request.getParameter("availability"));

        room newRoom = new room(roomID, roomName, capacity, location, availability);
        dao.insertRoom(newRoom);

        // Refresh page
        response.sendRedirect("admin.jsp");
    }

    private void editRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomID = request.getParameter("roomID");
        String roomName = request.getParameter("roomName");
        int capacity = Integer.parseInt(request.getParameter("capacity"));
        String location = request.getParameter("location");
        boolean availability = Boolean.parseBoolean(request.getParameter("availability"));

        room existingRoom = new room(roomID, roomName, capacity, location, availability);
        dao.updateRoom(existingRoom);

        // Refresh page
        response.sendRedirect("admin.jsp");
    }

    private void deleteRoom(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String roomID = request.getParameter("roomID");
        dao.deleteRoom(roomID);

        // Refresh page
        response.sendRedirect("admin.jsp");
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
