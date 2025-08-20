package com.pahana.edu.billing.dao;

import com.pahana.edu.billing.model.User;
import java.util.List;

public interface IUserDAO {
    User validateUser(String username, String password);
    User getUserById(int id);
    List<User> getAllUsers();
    boolean addUser(User user);
    boolean deleteUser(int id);
}
