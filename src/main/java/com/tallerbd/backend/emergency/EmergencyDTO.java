package com.tallerbd.backend.emergency;

import java.util.ArrayList;
import java.util.List;

import com.tallerbd.backend.task.Task;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmergencyDTO{

    private Long id;
    
    private String title;

    private String inCharge;

    private Double longitude;

    private Double latitude;

    // private String creatorId;

    private List<Task> tasks = new ArrayList<>();
}