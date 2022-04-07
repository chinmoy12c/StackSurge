package com.stacksurge.StackSurge.Models;

import java.sql.Date;
import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

@Entity
public class Instance {
    @Id
    @GeneratedValue
    @NonNull
    int id;

    @NonNull
    @ManyToOne
    User user;

    @NonNull
    String caddyContainerId;

    @NonNull
    String stackContainerId;

    @NonNull
    String stackName;

    @NonNull
    int port;

    @CreationTimestamp
    @NonNull
    Timestamp creationTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCaddyContainerId() {
        return caddyContainerId;
    }

    public void setCaddyContainerId(String caddyContainerId) {
        this.caddyContainerId = caddyContainerId;
    }

    public Timestamp getCreationTime() {
        return creationTime;
    }

    public void setCreationTime(Timestamp creationTime) {
        this.creationTime = creationTime;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getStackContainerId() {
        return stackContainerId;
    }
    
    public void setStackContainerId(String stackContainerId) {
        this.stackContainerId = stackContainerId;
    }

    public String getStackName() {
        return stackName;
    }

    public void setStackName(String stackName) {
        this.stackName = stackName;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
