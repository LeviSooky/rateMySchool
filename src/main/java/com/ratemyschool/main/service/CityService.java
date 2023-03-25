package com.ratemyschool.main.service;

import com.ratemyschool.main.model.CityData;
import com.ratemyschool.main.repo.CityDataRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CityService {

    private final CityDataRepository repository;

    public List<CityData> findAllBy(String keyword) {
     return repository.findAllByNameContainingIgnoreCase(keyword);
    }
}
