package com.stacksurge.StackSurge.Models;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.NonNull;

@Entity
public class Instance {
    @Id
    @GeneratedValue
    int id;

    @NonNull
    @ManyToOne
    User user;

    @NonNull
    String stackContainerId;

    @NonNull
    @ManyToOne
    TechStack techStack;

    int port;

    @CreationTimestamp
    @NonNull
    Timestamp creationTime;

    @Transient
    String creationTimeRep;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public TechStack getTechStack() {
        return techStack;
    }

    public void setTechStack(TechStack techStack) {
        this.techStack = techStack;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCreationTimeRep() {
        return creationTimeRep;
    }

    public void setCreationTimeRep(String creationTimeRep) {
        this.creationTimeRep = creationTimeRep;
    }
}
