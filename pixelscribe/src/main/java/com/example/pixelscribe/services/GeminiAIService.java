package com.example.pixelscribe.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;
import java.util.Base64;
import java.io.IOException;

@Service
public class GeminiAIService {

    private final WebClient webClient;

    @Value("${gemini.api.key:}")
    private String apiKey;

    public GeminiAIService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .baseUrl("https://generativelanguage.googleapis.com/v1beta")
                .build();
    }

    public String analyzeImage(MultipartFile imageFile) throws IOException {
        long startTime = System.currentTimeMillis();

        try {
            // Convertir imagen a base64
            String base64Image = Base64.getEncoder().encodeToString(imageFile.getBytes());

            // Crear request body para Gemini
            GeminiRequest request = createGeminiRequest(base64Image, imageFile.getContentType());

            // Llamar a la API de Gemini
            GeminiResponse response = webClient.post()
                    .uri("/models/gemini-pro-vision:generateContent?key=" + apiKey)
                    .bodyValue(request)
                    .retrieve()
                    .bodyToMono(GeminiResponse.class)
                    .block(); // Síncrono para categoría Junior

            String description = extractDescription(response);
            long processingTime = System.currentTimeMillis() - startTime;

            System.out.println("✅ Análisis de IA completado en " + processingTime + "ms");
            return description;

        } catch (Exception e) {
            System.err.println("❌ Error en análisis de IA: " + e.getMessage());
            throw e;
        }
    }

    private GeminiRequest createGeminiRequest(String base64Image, String mimeType) {
        GeminiRequest request = new GeminiRequest();

        Content content = new Content();
        Part[] parts = new Part[2];

        // Parte 1: Texto del prompt
        Part textPart = new Part();
        textPart.setText("Describe esta imagen de manera concisa y detallada en español. Responde solo con la descripción, sin prefijos como 'La imagen muestra'.");
        parts[0] = textPart;

        // Parte 2: Imagen
        Part imagePart = new Part();
        InlineData inlineData = new InlineData();
        inlineData.setMimeType(mimeType != null ? mimeType : "image/jpeg");
        inlineData.setData(base64Image);
        imagePart.setInlineData(inlineData);
        parts[1] = imagePart;

        content.setParts(parts);
        request.setContents(new Content[]{content});

        // Configuración de seguridad y generación
        request.setSafetySettings(new SafetySetting[]{
                createSafetySetting("HARM_CATEGORY_HARASSMENT", "BLOCK_ONLY_HIGH"),
                createSafetySetting("HARM_CATEGORY_HATE_SPEECH", "BLOCK_ONLY_HIGH"),
                createSafetySetting("HARM_CATEGORY_SEXUALLY_EXPLICIT", "BLOCK_ONLY_HIGH"),
                createSafetySetting("HARM_CATEGORY_DANGEROUS_CONTENT", "BLOCK_ONLY_HIGH")
        });

        request.setGenerationConfig(new GenerationConfig());
        request.getGenerationConfig().setTemperature(0.4);
        request.getGenerationConfig().setTopK(32);
        request.getGenerationConfig().setTopP(0.95);
        request.getGenerationConfig().setMaxOutputTokens(256);

        return request;
    }

    private SafetySetting createSafetySetting(String category, String threshold) {
        SafetySetting setting = new SafetySetting();
        setting.setCategory(category);
        setting.setThreshold(threshold);
        return setting;
    }

    private String extractDescription(GeminiResponse response) {
        if (response != null &&
                response.getCandidates() != null &&
                response.getCandidates().length > 0 &&
                response.getCandidates()[0].getContent() != null &&
                response.getCandidates()[0].getContent().getParts() != null &&
                response.getCandidates()[0].getContent().getParts().length > 0) {

            String description = response.getCandidates()[0].getContent().getParts()[0].getText();
            return description != null ? description.trim() : "No se pudo generar descripción";
        }

        return "No se pudo generar una descripción para la imagen";
    }

    // Clases internas para el request/response de Gemini
    public static class GeminiRequest {
        private Content[] contents;
        private SafetySetting[] safetySettings;
        private GenerationConfig generationConfig;

        public Content[] getContents() { return contents; }
        public void setContents(Content[] contents) { this.contents = contents; }

        public SafetySetting[] getSafetySettings() { return safetySettings; }
        public void setSafetySettings(SafetySetting[] safetySettings) { this.safetySettings = safetySettings; }

        public GenerationConfig getGenerationConfig() { return generationConfig; }
        public void setGenerationConfig(GenerationConfig generationConfig) { this.generationConfig = generationConfig; }
    }

    public static class Content {
        private Part[] parts;
        public Part[] getParts() { return parts; }
        public void setParts(Part[] parts) { this.parts = parts; }
    }

    public static class Part {
        private String text;
        private InlineData inlineData;
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        public InlineData getInlineData() { return inlineData; }
        public void setInlineData(InlineData inlineData) { this.inlineData = inlineData; }
    }

    public static class InlineData {
        private String mimeType;
        private String data;
        public String getMimeType() { return mimeType; }
        public void setMimeType(String mimeType) { this.mimeType = mimeType; }
        public String getData() { return data; }
        public void setData(String data) { this.data = data; }
    }

    public static class SafetySetting {
        private String category;
        private String threshold;
        public String getCategory() { return category; }
        public void setCategory(String category) { this.category = category; }
        public String getThreshold() { return threshold; }
        public void setThreshold(String threshold) { this.threshold = threshold; }
    }

    public static class GenerationConfig {
        private Double temperature;
        private Integer topK;
        private Double topP;
        private Integer maxOutputTokens;

        public Double getTemperature() { return temperature; }
        public void setTemperature(Double temperature) { this.temperature = temperature; }
        public Integer getTopK() { return topK; }
        public void setTopK(Integer topK) { this.topK = topK; }
        public Double getTopP() { return topP; }
        public void setTopP(Double topP) { this.topP = topP; }
        public Integer getMaxOutputTokens() { return maxOutputTokens; }
        public void setMaxOutputTokens(Integer maxOutputTokens) { this.maxOutputTokens = maxOutputTokens; }
    }

    public static class GeminiResponse {
        private Candidate[] candidates;
        public Candidate[] getCandidates() { return candidates; }
        public void setCandidates(Candidate[] candidates) { this.candidates = candidates; }
    }

    public static class Candidate {
        private Content content;
        public Content getContent() { return content; }
        public void setContent(Content content) { this.content = content; }
    }
}