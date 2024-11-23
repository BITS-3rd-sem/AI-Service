package com.dococ.ai.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ResponseDTO {

    String observation;

    @JsonProperty("ai-recommendation")
    String recommendedSpecialization;
}
