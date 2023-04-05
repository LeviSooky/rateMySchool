package com.ratemyschool.main.dto;

import lombok.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import javax.annotation.concurrent.Immutable;
import java.util.List;
import java.util.stream.Collectors;

@Value
@Immutable
public class PageResult<T extends DomainRepresented<R>, R> {
    Integer totalPages;
    Long totalElements;
    List<R> content;

    public PageResult(Page<T> page) {
        totalPages = page.getTotalPages();
        totalElements = page.getTotalElements();
        content = page.getContent()
                .stream()
                .map(T::toDomainModel)
                .collect(Collectors.toList());
    }

    public ResponseEntity<List<R>> buildResponse() {
        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Total-Pages", totalPages.toString());
        httpHeaders.set("Total-Elements", totalElements.toString());
        return ResponseEntity
                .ok()
                .headers(httpHeaders)
                .body(content);
    }
}
