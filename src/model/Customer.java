package model;

public class Customer {
    private String username;
    private String fullName;
    private boolean student;

    public Customer(String username, String fullName, boolean student) {
        this.username = username;
        this.fullName = fullName;
        this.student = student;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public boolean isStudent() {
        return student;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public void setStudent(boolean student) {
        this.student = student;
    }
}
