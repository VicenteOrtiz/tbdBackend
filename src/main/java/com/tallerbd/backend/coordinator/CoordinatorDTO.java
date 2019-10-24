package com.tallerbd.backend.coordinator;

import com.tallerbd.backend.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatorDTO extends UserDTO{

    //     User atributes     //

    // private Long id;

    // private String firstname;

    // private String lastname;

    // private String email;

    // private String password;

    private String institution;
}