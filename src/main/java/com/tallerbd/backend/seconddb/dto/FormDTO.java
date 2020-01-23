package com.tallerbd.backend.seconddb.dto;

import java.util.ArrayList;
import java.util.List;

import com.tallerbd.backend.seconddb.domain.FormEquipment;
import com.tallerbd.backend.seconddb.domain.FormRequirement;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FormDTO{

    private int neededGender;
    
    private List<FormRequirement> formRequirements = new ArrayList<>();

    private List<FormEquipment> formEquipment = new ArrayList<>();
}