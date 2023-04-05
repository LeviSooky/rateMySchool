package com.ratemyschool.main.repository;

import com.ratemyschool.main.entity.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ImageRepository extends JpaRepository<ImageData, UUID> {
}
