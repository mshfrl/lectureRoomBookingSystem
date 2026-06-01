package com.lecturebooking.model;

public class Room {
    private String roomId;
    private String roomName;
    private String location;
    private int capacity;

    // Default Constructor
    public Room() {
    }

    
    public Room(String roomId, String roomName, String location, int capacity) {
        this.roomId = roomId;
        this.roomName = roomName;
        this.location = location;
        this.capacity = capacity;
    }

    //Getters and Setters 

    public String getRoomId() {
        return roomId;
    }

    public void setRoomId(String roomId) {
        this.roomId = roomId;
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

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }
}