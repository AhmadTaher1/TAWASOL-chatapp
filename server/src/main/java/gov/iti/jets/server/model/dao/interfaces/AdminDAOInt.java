package gov.iti.jets.server.model.dao.interfaces;


import shared.dto.Admin;

import java.sql.SQLException;
import java.util.List;

public interface AdminDAOInt {
    void addAdmin(Admin admin) throws SQLException;
    Admin getAdminById(int adminId) throws SQLException;
    List<shared.dto.Admin> getAllAdmins() throws SQLException;
    void updateAdmin(Admin admin) throws SQLException;
    void deleteAdmin(int adminId) throws SQLException;
    boolean validateAdminCredentials(String username, String passwordHash) throws SQLException;
    //public int getAdminId(String userName , String password) throws SQLException;
}
