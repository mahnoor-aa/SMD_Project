package com.example.firebase_lawyer;

public class PendingGig {
    String caseid;
    String casetitle;
    String gigid;
    String lawyerid;
    String requestedgigid;
    String useremail;
    String userfullname;
    String username;
    String phone;
    String gigtitle;

    public PendingGig() {}

    public PendingGig(String caseid, String casetitle, String gigid, String gigtitle, String lawyerid, String requestedgigid, String useremail, String userfullname, String username, String phone) {
        this.caseid = caseid;
        this.casetitle = casetitle;
        this.gigid = gigid;
        this.gigtitle = gigtitle;
        this.lawyerid = lawyerid;
        this.requestedgigid = requestedgigid;
        this.useremail = useremail;
        this.userfullname = userfullname;
        this.username = username;
        this.phone = phone;
    }

    public String getRequestedgigid() {
        return requestedgigid;
    }

    public String getCaseid() {
        return caseid;
    }

    public String getGigTitle() {
        return gigtitle;
    }

    public void setCaseid(String caseid) {
        this.caseid = caseid;
    }

    public void setGigTitle(String gigTitle) {
        this.gigtitle = gigTitle;
    }

    public String getCasetitle() {
        return casetitle;
    }

    public void setCasetitle(String casetitle) {
        this.casetitle = casetitle;
    }

    public String getGigId() {
        return gigid;
    }

    public void setGigId(String gigId) {
        this.gigid = gigId;
    }

    public String getLawyerId() {
        return lawyerid;
    }

    public void setLawyerId(String lawyerId) {
        this.lawyerid = lawyerId;
    }

    public String getUseremail() {
        return useremail;
    }

    public void setUseremail(String useremail) {
        this.useremail = useremail;
    }

    public String getUserfullname() {
        return userfullname;
    }

    public void setUserfullname(String userfullname) {
        this.userfullname = userfullname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
