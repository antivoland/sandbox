package com.glu.glutest.dao;

public interface ProfileDAO {
    Profile get(String userId);
    void save(Profile profile);
}
