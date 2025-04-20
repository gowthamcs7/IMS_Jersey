package org.gowtham.service;

import org.gowtham.model.User;
import org.gowtham.dao.UsersDAO;


import java.util.List;


public class UserService {
    private final UsersDAO usersDAO;

    public UserService() {
        this.usersDAO = new UsersDAO();
    }

    public boolean addUser(User user) {
        return usersDAO.createUser(user);
    }

    public User getUserById(int id) {
        User user = usersDAO.getUserById(id);
        if (user != null) {
            user.setFormattedCreatedAt(); // Convert timestamp before returning
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = usersDAO.getAllUsers();
        for (User user : users) {
            user.setFormattedCreatedAt(); // Manually format each user’s timestamp
        }
        return users;
    }



    public boolean updateUser(long id, User user) {
        return usersDAO.updateUser(id,user);
    }

    public boolean deleteUser(long id) {
        return usersDAO.deleteUser(id);
    }
}
