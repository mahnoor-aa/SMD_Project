package com.example.firebase_lawyer;

public class Lawyer {
    private String id;
    private String name;
    private String phone;
    private String email;
    private String picture;
    private String username;
    private String password;
    private String lawyerType;
    private String courtType;
    private String highestLicense;
    public Lawyer() {
    }

    public Lawyer(String id,String name, String email, String phone, String picture, String username, String password, String lawyerType, String courtType, String highestLicense) {
        this.id= id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.picture = picture;
        this.username = username;
        this.password = password;
        this.lawyerType = lawyerType;
        this.courtType = courtType;
        this.highestLicense = highestLicense;
    }

    // Getters and setters
    // (You can generate these methods automatically in most IDEs)

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
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

    public String getLawyerType() {
        return lawyerType;
    }

    public void setLawyerType(String lawyerType) {
        this.lawyerType = lawyerType;
    }

    public String getCourtType() {
        return courtType;
    }

    public void setCourtType(String courtType) {
        this.courtType = courtType;
    }

    public String getHighestLicense() {
        return highestLicense;
    }

    public void setHighestLicense(String highestLicense) {
        this.highestLicense = highestLicense;
    }
}
