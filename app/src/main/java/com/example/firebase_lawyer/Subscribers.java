package com.example.firebase_lawyer;
public class Subscribers {
    private String gigid;
    private int hearing;
    private String id;
    private String lawyerid;
    private String price;
    private String username;
    private String casetitle;

    public Subscribers() {
    }

    public Subscribers(String gigid, int hearing, String id, String lawyerid, String price, String username,String casetitle) {
        this.casetitle=casetitle;
        this.gigid = gigid;
        this.hearing = hearing;
        this.id = id;
        this.lawyerid = lawyerid;
        this.price = price;
        this.username = username;

    }

    public String getGigid() {
        return gigid;
    }

    public String getCasetitle() {
        return casetitle;
    }
    public void setCasetitle(String casetitle) {
        this.casetitle = casetitle;
    }

    public int getHearing() {
        return hearing;
    }

    public String getId() {
        return id;
    }

    public String getLawyerid() {
        return lawyerid;
    }

    public String getPrice() {
        return price;
    }

    public String getUsername() {
        return username;
    }

    public void setGigid(String gigid) {
        this.gigid = gigid;
    }

    public void setHearing(int hearing) {
        this.hearing = hearing;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setLawyerid(String lawyerid) {
        this.lawyerid = lawyerid;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
