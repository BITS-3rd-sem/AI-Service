package com.dococ.ai.controller;

import com.dococ.ai.dto.PromptDTO;
import com.dococ.ai.dto.ResponseDTO;
import com.dococ.ai.service.AIService;
import com.dococ.ai.service.AuthenticationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/ai")
public class AIController {

    @Autowired
    AIService aiService;

    @Autowired
    AuthenticationService authenticationService;

    @PostMapping
    ResponseEntity<ResponseDTO> getRecommendation(@RequestBody PromptDTO promptDTO, @RequestHeader String authorization) throws JsonProcessingException {
        authenticationService.validateRole(authorization, List.of("PATIENT"));

        List<String> availableSpecializations = aiService.getAvailableSpecializations(authorization);

        return ResponseEntity.ok().body(aiService.getSpecializationRecomendation(promptDTO, availableSpecializations));
    }
}
