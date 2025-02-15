package gov.iti.jets.server.model.dao.interfaces;


import shared.dto.User;

import java.util.List;
import java.util.Map;

public interface UserDAOInt {

    boolean createUser(User user);

    boolean createUserWithoutID(User user);

    User getUserById(int userId);

    boolean updateUser(User user);

    boolean deleteUser(int userId);

    List<User> getAllUsers();

    User getUserByPhoneNumber(String phoneNumber);

    boolean updateLastSeen(int userId);

    boolean updateBio(int userId, String bio);

    boolean updatePic(int userId, String picPath);

    boolean updateStatus(int userId, User.Status status);

    boolean updatePassword(int userId, String password);

    int countOnlineUsers(int minutesThreshold);

    int countAllUsers();

    int countCertainGenderUsers(String gender);


    int countCertainCountryUsers(String country);

    Map<String, Integer> countCountryUsers();

    String saveImg(byte[] img, String phoneNumber);
}
