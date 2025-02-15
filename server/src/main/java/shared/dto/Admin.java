package shared.dto;

import java.io.Serializable;
import java.sql.Date;

public class Admin implements Serializable {

    private int adminId;
    private String userName;
    private String phoneNumber;
    private String email;
    private String passwordHash;
    private Gender gender;
    private String country;
    private Date dateOfBirth;

    public enum Gender {
        male, female
    }

    public Admin() {
    }

    public Admin(int adminId, String userName, String phoneNumber, String email, String passwordHash, Gender gender, String country, Date dateOfBirth) {
        this.adminId = adminId;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
    }

    public Admin(String userName, String phoneNumber, String email, String passwordHash, Gender gender, String country, Date dateOfBirth) {

        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.passwordHash = passwordHash;
        this.gender = gender;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
    }

    public int getAdminId() {
        return adminId;
    }

    public void setAdminId(int adminId) {
        this.adminId = adminId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public Gender getGender() {
        return gender;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public String toString() {
        return String.format("Admin ID: %d, User Name: %s, Email: %s, Phone: %s, Gender: %s, Country: %s, DOB: %s",
                adminId, userName, email, phoneNumber, gender.name(), country, dateOfBirth.toString());
    }
}
