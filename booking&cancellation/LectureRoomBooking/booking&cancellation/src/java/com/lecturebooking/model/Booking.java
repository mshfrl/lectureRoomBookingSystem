package com.lecturebooking.model;

public class Booking {
    private String studentName; 
    private String roomName;
    private String location;
    private String status;
    private String bookingDate;
    private String bookingTime;
    private String purpose;
    private String supportingLecturer;

    // Default Constructor
    public Booking() {
    }

    
    public Booking(String studentName, String roomName, String location, String status, 
                   String bookingDate, String bookingTime, String purpose, String supportingLecturer) {
        this.studentName = studentName;
        this.roomName = roomName;
        this.location = location;
        this.status = status;
        this.bookingDate = bookingDate;
        this.bookingTime = bookingTime;
        this.purpose = purpose;
        this.supportingLecturer = supportingLecturer;
    }

    //Getters and Setters

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(String bookingDate) {
        this.bookingDate = bookingDate;
    }

    public String getBookingTime() {
        return bookingTime;
    }

    public void setBookingTime(String bookingTime) {
        this.bookingTime = bookingTime;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public String getSupportingLecturer() {
        return supportingLecturer;
    }

    public void setSupportingLecturer(String supportingLecturer) {
        this.supportingLecturer = supportingLecturer;
    }
}