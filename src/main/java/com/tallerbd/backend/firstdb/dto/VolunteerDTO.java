package com.tallerbd.backend.firstdb.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.tallerbd.backend.firstdb.domain.Dimension;
import com.tallerbd.backend.firstdb.domain.Requirement;
import com.tallerbd.backend.firstdb.dto.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerDTO extends UserDTO{

    private int gender;

    private String birth;

    private Double longitude;

    private Double latitude;

    private List<Dimension> dimensions = new ArrayList<>();

    private List<Requirement> requirements = new ArrayList<>();
}