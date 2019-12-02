package com.example.simpleRest.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Arrays;
import java.util.List;

@Document
public class DbUser {

    @Id
    private String id;
    private String username;
    private String password;
    private List<String> userRoles;

    public DbUser() {}

    public DbUser(String username, String password,List<String> userRoles) {
        this.username = username;
        this.password = password;

        if (userRoles.size() == 0){
            userRoles = Arrays.asList("USER");
        }
        this.userRoles = userRoles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getUserRoles() {
        return userRoles;
    }

    public void setUserRoles(List<String> userRoles) {
        this.userRoles = userRoles;
    }

    public String getId() {
        return id;
    }
}
