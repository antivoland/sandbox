package com.glu.glutest.dao;

import com.google.code.morphia.annotations.Entity;
import com.google.code.morphia.annotations.Id;
import com.google.code.morphia.annotations.Property;

@Entity(value = "profile", noClassnameStored = true)
public class Profile {
    @Id private String id;
    @Property private int pings;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getPings() {
        return pings;
    }

    public void setPings(int pings) {
        this.pings = pings;
    }
}
