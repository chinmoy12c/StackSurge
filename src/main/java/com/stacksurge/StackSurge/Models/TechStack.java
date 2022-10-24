package com.stacksurge.StackSurge.Models;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Transient;

import org.springframework.lang.NonNull;

@Entity
public class TechStack {
    @Id
    @GeneratedValue
    int id;

    @NonNull
    @Column(unique = true)
    String codename;

    @NonNull
    String name;

    @NonNull
    String description;

    boolean isPrimary;

    @Transient
    List<StackFeature> features;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodename() {
        return codename;
    }

    public void setCodename(String codename) {
        this.codename = codename;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StackFeature> getFeatures() {
        return features;
    }

    public void setFeatures(List<StackFeature> features) {
        this.features = features;
    }
}
