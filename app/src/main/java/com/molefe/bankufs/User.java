package com.molefe.bankufs;

import net.sourceforge.jtds.jdbc.DateTime;

import java.util.Date;

public class User {
    public String id;
    public String studentNumber;
    public String passIdNumber;
    public String staffNumber;
    public String teacher;
    public String destination;
    public String surname;
    public DateTime registrationDate;
    public int userTypeId;
    public String role;
    public String password;
    public String userName;
    public String normalizedUserName;
    public String email;
    public String normalizedEmail;
    public boolean emailConfirmed;
    public String passwordHash;
    public String securityStamp;
    public String concurrencyStamp;
    public String phoneNumber;
    public boolean phoneNumberConfirmed;
    public boolean twoFactorEnabled;
    public Date lockoutEnd;
    public boolean lockoutEnabled;
    public int accessFailedCount;


    // Getter and Setter for 'email'
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Getter and Setter for 'userName'
    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    // Getter and Setter for 'role'
    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    // Getter and Setter for 'id'
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }







}
