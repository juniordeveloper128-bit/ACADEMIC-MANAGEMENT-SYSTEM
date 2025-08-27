package com.example.youtubetranslate.api;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/translate")
@Validated
public class TranslateController {

    private final RestClient restClient;

    public TranslateController() {
        this.restClient = RestClient.builder()
                .baseUrl("https://libretranslate.de")
                .build();
    }

    public record TranslationRequest(
            @NotNull List<@NotBlank String> lines,
            @NotBlank String targetLanguage,
            String sourceLanguage
    ) {}

    public record TranslationResponse(List<String> translated) {}

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TranslationResponse> translate(@RequestBody @Validated TranslationRequest request) {
        List<String> translated = request.lines.stream()
                .map(line -> translateSingle(line, request.sourceLanguage, request.targetLanguage))
                .toList();

        return ResponseEntity.ok(new TranslationResponse(translated));
    }

    private String translateSingle(String text, String sourceLanguage, String targetLanguage) {
        try {
            Map<String, Object> body = new HashMap<>();
            body.put("q", text);
            body.put("target", targetLanguage);
            if (sourceLanguage != null && !sourceLanguage.isBlank()) {
                body.put("source", sourceLanguage);
            }
            body.put("format", "text");

            Map<?, ?> response = restClient.post()
                    .uri("/translate")
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(body)
                    .retrieve()
                    .body(Map.class);

            if (response != null && response.get("translatedText") != null) {
                return String.valueOf(response.get("translatedText"));
            }
        } catch (Exception e) {
            // Fallback to original text on error
        }
        return text;
    }
}

