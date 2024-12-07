package com.dococ.ai.service.impl;

import com.dococ.ai.dto.PromptDTO;
import com.dococ.ai.dto.ResponseDTO;
import com.dococ.ai.exceptions.IllegalDataException;
import com.dococ.ai.service.AIService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.MessageFormat;
import java.util.List;

@Service
public class AIServiceImpl implements AIService {

    @Autowired
    RestTemplate restTemplate;

    String apiKey = "AIzaSyBCsACiUxSFNrjU6jzSP4oeM7BNTVXeZ3Y";
    String apiURL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key=" + apiKey;

    @Override
    public ResponseDTO getSpecializationRecomendation(PromptDTO promptDTO, List<String> specializations) throws JsonProcessingException {
        if (promptDTO.getSymptom().isBlank()) {
            throw new IllegalDataException("Symptom cannot be empty");
        }
        String prompt = "I will provide you symptoms, give your detailed medical observation " +
                "The response should be in json format with observation and ai-recommendation as key " +
                "Remember, You cannot perform any tests, examine someone or give medicine suggestion, You can just give probable suggestion " +
                "Your observation text should be in the tone like you are a doctor speaking to a patient, this should not have any questions " +
                "Your observation should be stored in the key observation " +
                "Also suggest the most suitable medical specialization for the patient to consult. " +
                "The specialization should be from this list of specialization, List of specializations: {0}." +
                "Your specialization recommendation should be stored in the key ai-recommendation: and should only contain the specialization as text from the list I provided" +
                "Symptoms: {1}." +
                "There should not be any html or markdown formatting to your response.";

        prompt = MessageFormat.format(prompt, String.join(",", specializations), promptDTO.getSymptom());
        String requestBody = "{\"contents\":[{\"parts\":[{\"text\":\"" + prompt + "\"}]}]}";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> response = restTemplate.exchange(
                apiURL, HttpMethod.POST, entity, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            return getStringResponseFromJSON(response.getBody());  // Return the response body
        }

        return new ResponseDTO();
    }


    private ResponseDTO getStringResponseFromJSON(String responseBody) throws JsonProcessingException {
        if (responseBody != null) {
            JSONObject jsonResponse = new JSONObject(responseBody);

            JSONArray candidates = jsonResponse.getJSONArray("candidates");
            if (!candidates.isEmpty()) {
                JSONObject candidate = candidates.getJSONObject(0);
                JSONObject content = candidate.getJSONObject("content");
                JSONArray parts = content.getJSONArray("parts");

                if (!parts.isEmpty()) {
                    String res = parts.getJSONObject(0).getString("text");
                    int jsonStartIndex = res.indexOf('{');
                    int jsonEndIndex = res.indexOf('}');
                    res = res.substring(jsonStartIndex, jsonEndIndex + 1);
                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.readValue(res, ResponseDTO.class);
                }
            }
        }

        return null;
    }

    @Override
    public List<String> getAvailableSpecializations(String token) {
        String apiURL = "http://iam-service.default.svc.cluster.local:8081/api/v1/specialization";
        //String apiURL = "http://localhost:8081/api/v1/specialization";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(headers);

        ResponseEntity<List<String>> specializations = restTemplate.exchange(apiURL, HttpMethod.GET, entity, new ParameterizedTypeReference<List<String>>() {});

        return specializations.getBody();
    }

}
