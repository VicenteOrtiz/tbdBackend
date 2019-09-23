package com.tallerbd.backend.coordinator;

import com.tallerbd.backend.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CoordinatorDTO extends UserDTO{

    private String institution;
}