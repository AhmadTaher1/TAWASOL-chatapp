package gov.iti.jets.server.model.dao.implementations;

import gov.iti.jets.server.model.dao.interfaces.AdminDAOInt;
import shared.dto.Admin;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AdminDAOImpl implements AdminDAOInt {

    private Connection connection;

    public AdminDAOImpl(Connection connection) {
        this.connection = connection;
    }

    public void addAdmin(Admin admin) throws SQLException {
        String sql = "INSERT INTO Admin (user_name, phone_number, email, password_hash, gender, country, date_of_birth) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, admin.getUserName());
            stmt.setString(2, admin.getPhoneNumber());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPasswordHash());
            stmt.setString(5, admin.getGender().name());
            stmt.setString(6, admin.getCountry());
            stmt.setDate(7, admin.getDateOfBirth());
            stmt.executeUpdate();

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    admin.setAdminId(generatedKeys.getInt(1));
                }
            }
        }
    }

    public Admin getAdminById(int adminId) throws SQLException {
        String sql = "SELECT * FROM Admin WHERE admin_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Admin(
                        rs.getInt("admin_id"),
                        rs.getString("user_name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        Admin.Gender.valueOf(rs.getString("gender")),
                        rs.getString("country"),
                        rs.getDate("date_of_birth")
                );
            }
        }
        return null;
    }

    public List<Admin> getAllAdmins() throws SQLException {
        List<Admin> admins = new ArrayList<>();
        String sql = "SELECT * FROM Admin";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                admins.add(new Admin(
                        rs.getInt("admin_id"),
                        rs.getString("user_name"),
                        rs.getString("phone_number"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        Admin.Gender.valueOf(rs.getString("gender")),
                        rs.getString("country"),
                        rs.getDate("date_of_birth")
                ));
            }
        }
        return admins;
    }

    public void updateAdmin(Admin admin) throws SQLException {
        String sql = "UPDATE Admin SET user_name = ?, phone_number = ?, email = ?, password_hash = ?, gender = ?, country = ?, date_of_birth = ? WHERE admin_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, admin.getUserName());
            stmt.setString(2, admin.getPhoneNumber());
            stmt.setString(3, admin.getEmail());
            stmt.setString(4, admin.getPasswordHash());
            stmt.setString(5, admin.getGender().name());
            stmt.setString(6, admin.getCountry());
            stmt.setDate(7, admin.getDateOfBirth());
            stmt.setInt(8, admin.getAdminId());
            stmt.executeUpdate();
        }
    }

    public void deleteAdmin(int adminId) throws SQLException {
        String sql = "DELETE FROM Admin WHERE admin_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, adminId);
            stmt.executeUpdate();
        }
    }

    public boolean validateAdminCredentials(String username, String passwordHash) throws SQLException {
        String sql = "SELECT COUNT(*) FROM admin WHERE user_name = ? AND password_hash = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) == 1; // Returns true if a matching record is found
                }
            }
        }
        return false;
    }

   /* @Override
    public int getAdminId(String username, String passwordHash) throws SQLException {
        String sql = "SELECT admin_id FROM admin WHERE user_name = ? AND password_hash = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, username);
            stmt.setString(2, passwordHash);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1); // Returns true if a matching record is found
                }
            }
        }
        return -1;
    }
    */
}

