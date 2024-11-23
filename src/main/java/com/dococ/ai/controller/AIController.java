package com.dococ.ai.controller;

import com.dococ.ai.dto.PromptDTO;
import com.dococ.ai.dto.ResponseDTO;
import com.dococ.ai.service.AIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ai-service")
public class AIController {

    @Autowired
    AIService aiService;

    @PostMapping
    ResponseDTO getRecommendation(@RequestBody PromptDTO promptDTO) throws JsonProcessingException {
        return aiService.getSpecializationRecomendation(promptDTO);
    }
}
