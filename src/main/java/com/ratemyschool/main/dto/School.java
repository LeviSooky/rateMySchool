package com.ratemyschool.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class School {
    private UUID id;
    @NotBlank
    private String name;
    @Pattern(regexp = "^(http(s):\\/\\/.)[-a-zA-Z0-9@:%._\\+~#=]{2,256}\\.[a-z]{2,6}\\b([-a-zA-Z0-9@:%_\\+.~#?&//=]*)$")
    private String websiteUrl;
}
