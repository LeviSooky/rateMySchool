package com.ratemyschool.main.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Getter
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SchoolCreateModel {
    private UUID id;
    @NotBlank
    private String name;
    @Pattern(regexp = "^(http(s):\\/\\/.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$")
    private String websiteUrl;

    private Float avgRating;

    private MultipartFile image;



}

