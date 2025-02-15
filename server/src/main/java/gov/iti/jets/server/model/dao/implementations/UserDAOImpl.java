package gov.iti.jets.server.model.dao.implementations;



import gov.iti.jets.server.model.dao.interfaces.UserDAOInt;
import shared.dto.User;
import shared.utils.DB_UtilityClass;
import shared.utils.Hashing_UtilityClass;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.*;
import java.util.*;

public class UserDAOImpl implements UserDAOInt {

    public boolean createUser(User user)  {

        boolean result =false;
        String sql = "INSERT INTO user (phone_number, display_name, email, password_hash, profile_picture_path, gender, country, date_of_birth, bio, status, last_seen) " + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection= DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getPhoneNumber());
            stmt.setString(2, user.getDisplayName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPasswordHash());
            stmt.setString(5, user.getProfilePicturePath());
            stmt.setString(6, user.getGender().toString());
            stmt.setString(7, user.getCountry());
            stmt.setDate(8, user.getDateOfBirth());
            stmt.setString(9, user.getBio());
            stmt.setString(10, user.getStatus().toString());
            stmt.setTimestamp(11, user.getLastSeen());
            result= stmt.executeUpdate()>0 ;
        }
        catch (Exception e){
            e.printStackTrace();
        }

        return result;

    }

    public boolean createUserWithoutID(User user) {
        boolean result = false;
        String sql = "INSERT INTO user (phone_number, display_name, email, password_hash, profile_picture_path, gender, country, date_of_birth, bio, status, last_seen) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {


            stmt.setString(1, user.getPhoneNumber());
            stmt.setString(2, user.getDisplayName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPasswordHash());
            stmt.setString(5, user.getProfilePicturePath());
            stmt.setString(6, user.getGender().toString());
            stmt.setString(7, user.getCountry());
            stmt.setDate(8, user.getDateOfBirth());
            stmt.setString(9, user.getBio());
            stmt.setString(10, user.getStatus().toString());
            stmt.setTimestamp(11, user.getLastSeen());

            result = stmt.executeUpdate() > 0;

            try (ResultSet generatedKeys = stmt.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    int generatedId = generatedKeys.getInt(1);
                    user.setUserId(generatedId);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }



    public User getUserById(int userId) {
        User user = null;
        String sql = "SELECT * FROM user WHERE user_id = ?";

        try ( Connection connection = DB_UtilityClass.getConnection();
              PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getString("phone_number"),
                            rs.getString("display_name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("profile_picture_path"),
                            User.Gender.valueOf(rs.getString("gender")),
                            rs.getString("country"),
                            rs.getDate("date_of_birth"),
                            rs.getString("bio"),
                            User.Status.valueOf(rs.getString("status").toUpperCase()),
                            rs.getTimestamp("last_seen")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return user;
    }




    public boolean updateUser(User user) {
        boolean result = false;
        String sql = "UPDATE user SET phone_number = ?, display_name = ?, email = ?, password_hash = ?, " +
                "profile_picture_path = ?, gender = ?, country = ?, date_of_birth = ?, bio = ?, status = ?, last_seen = ? " +
                "WHERE user_id = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, user.getPhoneNumber());
            stmt.setString(2, user.getDisplayName());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getPasswordHash());
            stmt.setString(5, user.getProfilePicturePath());
            stmt.setString(6, user.getGender().toString());
            stmt.setString(7, user.getCountry());
            stmt.setDate(8, user.getDateOfBirth());
            stmt.setString(9, user.getBio());
            stmt.setString(10, user.getStatus().toString());
            stmt.setTimestamp(11, user.getLastSeen());
            stmt.setInt(12, user.getUserId());
            result = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }


        return result;
    }

    public boolean editUserShownInfo(int userId, String name, User.Status status, String picPath, String bio) {
        boolean result = false;
        String sql = "UPDATE user SET display_name = ?,status = ?,profile_picture_path = ?,bio = ? WHERE user_id = ?";

        System.out.println("Executing SQL: " + sql);
        System.out.println("Parameters: " + name + ", " + status + ", " + picPath + ", " + bio + ", " + userId);

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, status.toString().toLowerCase());
            stmt.setString(3, picPath);
            stmt.setString(4, bio);
            stmt.setInt(5, userId);
            result = stmt.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("there is an error in teh editUserShowInfo in dao");
            e.printStackTrace();
        }

        return result;
    }


    public boolean deleteUser(int userId) {
        boolean result = false;
        String sql = "DELETE FROM user WHERE user_id = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            result = stmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

       //the delete method which resets the id after deleting so that there will be no gaps

//    public boolean deleteUser(int userId) {
//        boolean result = false;
//        String sql = "DELETE FROM user WHERE user_id = ?";
//        String getMaxUserIdSql = "SELECT IFNULL(MAX(user_id), 0) + 1 AS nextAutoIncrement FROM user";
//        String resetAutoIncrementSql = "ALTER TABLE user AUTO_INCREMENT = ?";
//
//        try (Connection connection = DB_UtilityClass.getConnection();
//             PreparedStatement stmt = connection.prepareStatement(sql);
//             PreparedStatement resetStmt = connection.prepareStatement(resetAutoIncrementSql);
//             Statement getMaxStmt = connection.createStatement()) {
//
//            // Delete the user
//            stmt.setInt(1, userId);
//            result = stmt.executeUpdate() > 0;
//
//            // Reset AUTO_INCREMENT if the deletion was successful
//            if (result) {
//                ResultSet rs = getMaxStmt.executeQuery(getMaxUserIdSql);
//                if (rs.next()) {
//                    int nextAutoIncrement = rs.getInt("nextAutoIncrement");
//                    resetStmt.setInt(1, nextAutoIncrement);
//                    resetStmt.executeUpdate();
//                }
//            }
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//        return result;
//    }


    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM user";

        try (Connection connection = DB_UtilityClass.getConnection();
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql))
        {
            while (rs.next()) {
                User user = new User(
                        rs.getInt("user_id"),
                        rs.getString("phone_number"),
                        rs.getString("display_name"),
                        rs.getString("email"),
                        rs.getString("password_hash"),
                        rs.getString("profile_picture_path"),
                        User.Gender.valueOf(rs.getString("gender")),
                        rs.getString("country"),
                        rs.getDate("date_of_birth"),
                        rs.getString("bio"),
                        User.Status.valueOf(rs.getString("status").toUpperCase()),
                        rs.getTimestamp("last_seen")
                );
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    public boolean isUserFoundByPhoneNumber(String phone_number) {
        String sql = "SELECT * FROM user WHERE phone_number = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, phone_number);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean isUserFoundByEmail(String email) {
        String sql = "SELECT * FROM user WHERE email = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, email);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public User getUserByPhoneNumber(String phone_number) {

        User user = null;
        String sql = "SELECT * FROM user WHERE phone_number = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, phone_number);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getString("phone_number"),
                            rs.getString("display_name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("profile_picture_path"),
                            User.Gender.valueOf(rs.getString("gender")),
                            rs.getString("country"),
                            rs.getDate("date_of_birth"),
                            rs.getString("bio"),
                            User.Status.valueOf(rs.getString("status").toUpperCase()),
                            rs.getTimestamp("last_seen")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public boolean updateLastSeen(int userId) {
        boolean result = false;
        String sql = "UPDATE user SET last_seen = CURRENT_TIMESTAMP WHERE user_id = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            result = stmt.executeUpdate() > 0;
         } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean updateBio(int userId,String bio) {
        boolean result = false;
        String sql = "UPDATE user SET bio = ? WHERE user_id = ?";

        try ( Connection connection = DB_UtilityClass.getConnection();
              PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, bio);
            stmt.setInt(2, userId);
            result = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }



    public boolean updatePic(int userId,String picPath) {

        boolean result = false;
        String sql = "UPDATE user SET profile_picture_path = ? user_id = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, picPath);
            stmt.setInt(2, userId);
            result = stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return result;
    }

    public boolean updateStatus(int userId, User.Status status) {

        boolean result = false;
        String sql = "UPDATE user SET status = ? WHERE user_id = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, status.toString());
            stmt.setInt(2, userId);
            result = stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }



    public boolean updatePassword(int userId, String password) {
        boolean result = false;
        String sql = "UPDATE user SET password_hash = ? WHERE user_id = ?";
        String hashedPassword= Hashing_UtilityClass.hashString(password);

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, hashedPassword);
            stmt.setInt(2, userId);
            result = stmt.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public int countOnlineUsers(int minutesThreshold) {
        int onlineCount = 0;
        String sql = "SELECT COUNT(*) FROM user WHERE last_seen >= NOW() - INTERVAL ? MINUTE";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setInt(1, minutesThreshold);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    onlineCount = rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return onlineCount;
    }

    public int countAllUsers (){

         return  getAllUsers().size();
    }

    public int countCertainGenderUsers(String gender) {

        int Count = 0;
        String sql =  "SELECT COUNT(*) FROM user WHERE gender =?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql))
        {
            stmt.setString(1 , gender);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Count=rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Count;
    }


    public int countCertainCountryUsers(String country) {

        int Count = 0;
        System.out.println(Count);
        String sql =  "SELECT COUNT(*) FROM user WHERE country = ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1,country );
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {

                    Count=rs.getInt(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return Count;
    }

    public Map<String, Integer> countCountryUsers() {

        Map<String, Integer> map = new HashMap<>();
        String sql = "SELECT country, COUNT(*) FROM user GROUP BY country";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery())
        {

            while (rs.next()) {
                String country = rs.getString("country");
                int count = rs.getInt(2);
                map.put(country, count);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return map;
    }

    public String saveImg(byte[] img, String phoneNumber) {
        FileOutputStream fos = null;
        String imgID = UUID.randomUUID().toString();    //creates unique id for each image
        String path = "./files/imgs/" + phoneNumber;
        try {
            File theDir = new File(path);
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            fos = new FileOutputStream(path + "/" + imgID + ".jpg");
            fos.write(img);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fos.close();
            } catch ( IOException e) {
                e.printStackTrace();
            }
        }
        return path + "/" + imgID + ".jpg";
    }

    public User getUserByPhoneNumberAndPassword(String phone_number, String password) {
        User user = null;
        String hashed_password = Hashing_UtilityClass.hashString(password);
        String sql = "SELECT * FROM user WHERE phone_number = ? And password_hash= ?";

        try (Connection connection = DB_UtilityClass.getConnection();
             PreparedStatement stmt = connection.prepareStatement(sql)) {

            stmt.setString(1, phone_number);
            stmt.setString(2, hashed_password);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    user = new User(
                            rs.getInt("user_id"),
                            rs.getString("phone_number"),
                            rs.getString("display_name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getString("profile_picture_path"),
                            User.Gender.valueOf(rs.getString("gender")),
                            rs.getString("country"),
                            rs.getDate("date_of_birth"),
                            rs.getString("bio"),
                            User.Status.valueOf(rs.getString("status").toUpperCase()),
                            rs.getTimestamp("last_seen")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }


}
