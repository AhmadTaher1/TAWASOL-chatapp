package shared.dto;

import java.io.Serializable;
import java.sql.Timestamp;
import java.sql.Date;

public class User implements Serializable {
    private int userId;
    private String phoneNumber;
    private String displayName;
    private String email;
    private String passwordHash;
    private String profilePicturePath;
    private Gender gender;                        // Enum for 'male' or 'female'
    private String country;
    private Date dateOfBirth;
    private String bio;
    private Status status;                        // Enum for 'available', 'busy', 'away', 'OFFLINE'
    private Timestamp lastSeen;

    // Enum for Gender
    public enum Gender {
        male, female
    }

    // Enum for Status
    public enum Status {
        AVAILABLE, BUSY, AWAY, OFFLINE
    }

    // Default constructor
    public User() {
    }

    // Overloaded constructor
    public User(int userId, String phoneNumber, String displayName, String email, String passwordHash,
                String profilePicturePath, Gender gender, String country, java.sql.Date dateOfBirth, String bio,
                Status status, Timestamp lastSeen) {
        this.userId = userId;
        this.phoneNumber = phoneNumber;
        this.displayName = displayName;
        this.email = email;
        this.passwordHash = passwordHash;
        this.profilePicturePath = profilePicturePath;
        this.gender = gender;
        this.country = country;
        this.dateOfBirth = dateOfBirth;
        this.bio = bio;
        this.status = status;
        this.lastSeen = lastSeen;
    }

    // Getters and setters
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
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

    public String getProfilePicturePath() {
        return profilePicturePath;
    }

    public void setProfilePicturePath(String profilePicturePath) {
        this.profilePicturePath = profilePicturePath;
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

    public java.sql.Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(java.sql.Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Timestamp getLastSeen() {
        return lastSeen;
    }

    public void setLastSeen(Timestamp lastSeen) {
        this.lastSeen = lastSeen;
    }

    // Overridden toString() method
    @Override
    public String toString() {
        return "User{" +
                "userId=" + userId +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", displayName='" + displayName + '\'' +
                ", email='" + email + '\'' +
                ", passwordHash='" + passwordHash + '\'' +
                ", profilePicturePath='" + profilePicturePath + '\'' +
                ", gender=" + gender +
                ", country='" + country + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", bio='" + bio + '\'' +
                ", status=" + status +
                ", lastSeen=" + lastSeen +
                '}';
    }
}
