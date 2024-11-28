package com.dococ.ai.service;

import com.dococ.ai.dto.PromptDTO;
import com.dococ.ai.dto.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.List;

public interface AIService {

    ResponseDTO getSpecializationRecomendation(PromptDTO promptDTO, List<String> specializations) throws JsonProcessingException;

    List<String> getAvailableSpecializations(String token);
}
