package com.example.esraa.fitpass.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class UserModel {
    //    List<GymModel> favouriteGyms;
    private String userId;
    private String email;
    private String password;
    private HashMap<String, ClassModel> userClasses;
    private HashMap<String, GymModel> favouriteGyms;
    private boolean isHasPackage;
    private String pushedKey;
//    private List<ClassModel> userClasses;

//    public List<ClassModel> getUserClasses() {
//        return userClasses;
//    }
//
//    public void setUserClasses(List<ClassModel> userClasses) {
//        this.userClasses = userClasses;
//    }

//    public List<GymModel> getFavouriteGyms() {
//        return favouriteGyms;
//    }
//
//    public void setFavouriteGyms(List<GymModel> favouriteGyms) {
//        this.favouriteGyms = favouriteGyms;
//    }

    public String getPushedKey() {
        return pushedKey;
    }

    public void setPushedKey(String pushedKey) {
        this.pushedKey = pushedKey;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public List<ClassModel> getUserClasses() {
        if (userClasses != null) {
            return new ArrayList<>(userClasses.values());
        } else {
            return null;
        }
    }

    public String getPassword() {
        return password;
    }

    public List<GymModel> getFavouriteGyms() {
        if (favouriteGyms != null) {
            return new ArrayList<>(favouriteGyms.values());
        } else {
            return null;
        }
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isHasPackage() {
        return isHasPackage;
    }

    public void setHasPackage(boolean hasPackage) {
        isHasPackage = hasPackage;
    }
}
