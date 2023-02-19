package com.ratemyschool.main.model;

@FunctionalInterface
public interface DomainRepresented<T> {
    T toDomainModel();
}
