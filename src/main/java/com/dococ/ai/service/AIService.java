package com.dococ.ai.service;

import com.dococ.ai.dto.PromptDTO;
import com.dococ.ai.dto.ResponseDTO;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface AIService {

    ResponseDTO getSpecializationRecomendation(PromptDTO promptDTO) throws JsonProcessingException;
}
