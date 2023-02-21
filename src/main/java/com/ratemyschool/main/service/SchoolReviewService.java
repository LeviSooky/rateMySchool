package com.ratemyschool.main.service;

import com.ratemyschool.main.dto.SchoolReview;
import com.ratemyschool.main.model.PageResult;
import com.ratemyschool.main.model.SchoolReviewData;
import com.ratemyschool.main.repo.SchoolReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class SchoolReviewService {

    private final SchoolReviewRepository repository;

    public PageResult<SchoolReviewData, SchoolReview> findAllActiveBy(UUID id, Pageable pageable) {
        return new PageResult<>(repository.findAllActiveBy(id, pageable));
    }
}
