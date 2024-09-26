package com.example.eventbrite.Models;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String userId;
    private String name;
    private String email;
    private String about;
    private List<String> followers;
    private List<String> following;
    private String userType;

    public User(){
        this.followers = new ArrayList<>(); // Initialize to avoid null
        this.following = new ArrayList<>(); // Initialize to avoid null

    }

    public User(String userId, String name, String email, String about, List<String> followers, List<String> following, String userType) {
        this.userId = userId;
        this.name = name;
        this.email = email;
        this.about = about;
        this.followers = followers != null ? followers : new ArrayList<>(); // Avoid null
        this.following = following != null ? following : new ArrayList<>(); // Avoid null
        this.userType = userType;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public List<String> getFollowers() {
        return followers;
    }

    public void setFollowers(List<String> followers) {
        this.followers = followers != null ? followers : new ArrayList<>(); // Avoid null
    }

    public List<String> getFollowing() {
        return following;
    }

    public void setFollowing(List<String> following) {
        this.following = following != null ? following : new ArrayList<>(); // Avoid null
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
