package com.stacksurge.StackSurge.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stacksurge.StackSurge.Models.StackFeature;
import com.stacksurge.StackSurge.Models.TechStack;

public interface StackFeatureRepo extends JpaRepository<StackFeature, Integer>{
    
    public List<StackFeature> getByTechStack(TechStack techStack);

    public default List<StackFeature> getStackFeatures(TechStack techStack) {
        List<StackFeature> features = getByTechStack(techStack);
        for (StackFeature feature : features)
            feature.setTechStack(null);
        return features;
    }
}
