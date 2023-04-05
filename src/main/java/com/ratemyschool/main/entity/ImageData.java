package com.ratemyschool.main.entity;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "image")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ImageData {

    @Id
    private UUID id;

    @Lob
    private byte[] image;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        ImageData imageData = (ImageData) o;
        return id != null && Objects.equals(id, imageData.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
