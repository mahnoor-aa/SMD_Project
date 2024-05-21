package com.example.firebase_lawyer;
public class GigClass {
    private String id;
    private String title;
    private String desc;
    private int price;
    private String IBAN;
    private String lawyerid;
    private String name;
    private String email;
    private String phoneno;
    private String lawyertype;
    private String courttype;
    private String city;
    private String highestlicense;

    public GigClass() {
        // Default constructor required for Firebase
    }

    public GigClass(String id, String title, String desc, int price, String IBAN, String lawyerid, String name,
                    String email, String phoneno, String lawyertype, String courttype, String highestlicense,String city) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.price = price;
        this.IBAN = IBAN;
        this.lawyerid = lawyerid;
        this.name = name;
        this.email = email;
        this.phoneno = phoneno;
        this.lawyertype = lawyertype;
        this.courttype = courttype;
        this.highestlicense = highestlicense;
        this.city=city;
    }

    public String getId() {
        return id;
    }

    public String getCity() {
        return city;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getIBAN() {
        return IBAN;
    }

    public void setIBAN(String IBAN) {
        this.IBAN = IBAN;
    }

    public void setCity(String city) {
        this.city = city;
    }


    public String getLawyerid() {
        return lawyerid;
    }

    public void setLawyerid(String lawyerid) {
        this.lawyerid = lawyerid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneno() {
        return phoneno;
    }

    public void setPhoneno(String phoneno) {
        this.phoneno = phoneno;
    }

    public String getLawyertype() {
        return lawyertype;
    }

    public void setLawyertype(String lawyertype) {
        this.lawyertype = lawyertype;
    }

    public String getCourttype() {
        return courttype;
    }

    public void setCourttype(String courttype) {
        this.courttype = courttype;
    }

    public String getHighestlicense() {
        return highestlicense;
    }

    public void setHighestlicense(String highestlicense) {
        this.highestlicense = highestlicense;
    }
}
