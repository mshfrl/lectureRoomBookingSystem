/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.lab.model;

/**
 *
 * @author Asus
 */
public class Users {
    private int id;
    private String username;
    private String password;
    private String role;

    //constructor kosong (wajib untuk javabean)
    public Users() {

    }

    //constructor dengan ID (untuk update dan delete)
    public Users(String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
        
    }

    //constructor tanpa id (untuk insert)
    /*public Users(String username, String password) {
        this.username = username;
        this.password = password;
    }*/

    // Getter dan Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRoles() {
        return role;
    }

    public void setRoles(String roles) {
        this.role = role;
    }
}
