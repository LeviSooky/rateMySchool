package com.ratemyschool.main.dto;

import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.entity.CityData;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class School {
    private UUID id;
    @NotBlank
    @Size(min = 10)
    private String name;
    @Pattern(regexp = "^(http(s):\\/\\/.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$")
    private String websiteUrl;
    private Float avgRating;
    private EntityStatus status;
    private CityData city;

}
