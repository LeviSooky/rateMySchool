package com.ratemyschool.main.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
public class RMSReview {
    @Id
    private UUID id;
    private String content;
    private String statusFlag;
    private LocalDateTime creationDate;
    @ManyToOne
    private RMSTeacher teacher;

}
