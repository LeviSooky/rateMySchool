package com.ratemyschool.main.model;

import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class Review {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String content;
    private UUID deleteKey;
    private byte stars;
    private String statusFlag;
    @CreatedDate
    private LocalDateTime creationDate;
    @ManyToOne
    private Teacher teacher;

}
