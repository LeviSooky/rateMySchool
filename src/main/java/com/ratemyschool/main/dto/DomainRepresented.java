package com.ratemyschool.main.dto;

@FunctionalInterface
public interface DomainRepresented<T> {
    T toDomainModel();
}
