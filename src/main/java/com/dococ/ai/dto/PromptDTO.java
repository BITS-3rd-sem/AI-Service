package com.dococ.ai.dto;

import lombok.Data;

import java.util.List;

@Data
public class PromptDTO {

    String symptom;

    List<String> specializations;
}
