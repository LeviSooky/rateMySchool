package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.CityData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CityDataRepository extends JpaRepository<CityData, UUID> {

    public List<CityData> findAllByNameContainingIgnoreCase(String keyword);
}
