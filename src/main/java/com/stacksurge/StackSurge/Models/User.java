package com.stacksurge.StackSurge.Models;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

@Entity
public class User {
    @Id
    @GeneratedValue
    int id;

    @NonNull
    String email;

    @NonNull
    String password;

    @NonNull
    String caddyPass;

    @NonNull
    String volume;

    @NonNull
    @CreationTimestamp
    Timestamp regDate;

    @NonNull
    String type;

    @OneToMany(mappedBy = "user")
    List<Instance> instances;

    public String getCaddyPass() {
        return caddyPass;
    }

    public void setCaddyPass(String caddyPass) {
        this.caddyPass = caddyPass;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Instance> getInstances() {
        return instances;
    }

    public void setInstances(List<Instance> instances) {
        this.instances = instances;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Timestamp getRegDate() {
        return regDate;
    }

    public void setRegDate(Timestamp regDate) {
        this.regDate = regDate;
    }

    public String getType() {
        return type;
    }
    
    public void setType(String type) {
        this.type = type;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }
}
