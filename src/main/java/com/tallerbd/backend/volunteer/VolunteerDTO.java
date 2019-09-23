package com.tallerbd.backend.volunteer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import com.tallerbd.backend.dimension.Dimension;
import com.tallerbd.backend.requirement.Requirement;
import com.tallerbd.backend.user.UserDTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VolunteerDTO extends UserDTO{

    private int gender;

    private String birth;

    private Float latitude;

    private Float longitude;

    private List<Dimension> dimensions = new ArrayList<>();

    private List<Requirement> requirements = new ArrayList<>();
}