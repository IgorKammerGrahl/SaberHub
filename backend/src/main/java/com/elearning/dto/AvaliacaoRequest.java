package com.elearning.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AvaliacaoRequest {
    @Min(1) @Max(5)
    private Integer nota;
    
    @Size(max = 500)
    private String comentario;
}