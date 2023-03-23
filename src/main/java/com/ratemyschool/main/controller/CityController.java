package com.ratemyschool.main.controller;


import com.ratemyschool.main.model.CityData;
import com.ratemyschool.main.service.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("api/city")
public class CityController {

    private final CityService cityService;

    @GetMapping("/{keyword}")
    public ResponseEntity<List<CityData>> findAllBy(@PathVariable String keyword) {
       log.info("REST request to search for cities by: {}", keyword);
       return ResponseEntity.ok(cityService.findAllBy(keyword));
    }
}
