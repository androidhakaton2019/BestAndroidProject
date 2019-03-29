package msk.android.academy.javatemplate.ui.user;

public class User {

    private Integer id;
    private String userId;
    private String name;
    private Status userStatus;
    private String phoneNum;
    private String address;
    private String token;
    private String message;
    private int sits;
    private double lon;
    private double lat;

    public User() {

    }

    public User(String name, String phoneNum, String address, String token, double lat, double lon,
                int sits, String message, String userId) {
        this.name = name;
        this.message = message;
        this.lat = lat;
        this.lon = lon;
        this.phoneNum = phoneNum;
        this.address = address;
        this.token = token;
        this.userStatus = Status.USER;
        this.sits = sits;
        this.userId = userId;
    }

    public User(String name, String phoneNum, String address, String userId) {
        this.name = name;
        this.phoneNum = phoneNum;
        this.address = address;
        this.userStatus = Status.USER;
        this.userId = userId;
    }


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Status getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(Status userStatus) {
        this.userStatus = userStatus;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public int getSits() {
        return sits;
    }

    public void setSits(int sits) {
        this.sits = sits;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public enum Status {
        USER,
        DRIVER,
        RIDER
    }

}
