package com.ratemyschool.main.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import net.minidev.json.annotate.JsonIgnore;

import java.util.UUID;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class User {

    private UUID id;
    private String email;
    private boolean isAdmin;
    private String lastName;
    private String firstName;
    @JsonIgnore
    private String password;
}
