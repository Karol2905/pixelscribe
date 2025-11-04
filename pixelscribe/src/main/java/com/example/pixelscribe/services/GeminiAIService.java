package com.example.pixelscribe.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

@Service
public class GeminiAIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    // 🔥 Usa gemini-pro-vision con v1beta
    private static final String API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent";

    private static final String MODEL_NAME = "gemini-pro-vision";

    private final RestTemplate restTemplate;

    public GeminiAIService() {
        this.restTemplate = new RestTemplate();
    }

    public String analyzeImage(MultipartFile imageFile) throws Exception {
        try {
            // Convertir imagen a Base64
            byte[] imageBytes = imageFile.getBytes();
            String base64Image = Base64.getEncoder().encodeToString(imageBytes);

            // Obtener el tipo MIME
            String mimeType = imageFile.getContentType();

            // Construir el request body
            Map<String, Object> requestBody = buildRequestBody(base64Image, mimeType);

            // Configurar headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construir URL con API key
            String urlWithKey = API_URL + "?key=" + apiKey;

            System.out.println("🔍 Llamando a Gemini API...");
            System.out.println("📦 Tamaño de imagen: " + imageBytes.length + " bytes");
            System.out.println("🎨 Tipo MIME: " + mimeType);

            // Hacer la petición
            HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                    urlWithKey,
                    HttpMethod.POST,
                    request,
                    Map.class
            );

            System.out.println("✅ Respuesta recibida de Gemini");

            // Extraer la descripción de la respuesta
            return extractDescription(response.getBody());

        } catch (Exception e) {
            System.err.println("❌ Error completo en Gemini API: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("Error al analizar la imagen con Gemini: " + e.getMessage(), e);
        }
    }

    private Map<String, Object> buildRequestBody(String base64Image, String mimeType) {
        Map<String, Object> requestBody = new HashMap<>();

        // Contents array
        List<Map<String, Object>> contents = new ArrayList<>();
        Map<String, Object> content = new HashMap<>();

        // Parts array
        List<Map<String, Object>> parts = new ArrayList<>();

        // Text prompt
        Map<String, Object> textPart = new HashMap<>();
        textPart.put("text", "Describe esta imagen de forma detallada en español.");
        parts.add(textPart);

        // Image data
        Map<String, Object> imagePart = new HashMap<>();
        Map<String, Object> inlineData = new HashMap<>();
        inlineData.put("mime_type", mimeType);
        inlineData.put("data", base64Image);
        imagePart.put("inline_data", inlineData);
        parts.add(imagePart);

        content.put("parts", parts);
        contents.add(content);

        requestBody.put("contents", contents);

        return requestBody;
    }

    private String extractDescription(Map<String, Object> responseBody) {
        try {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) responseBody.get("candidates");
            if (candidates != null && !candidates.isEmpty()) {
                Map<String, Object> firstCandidate = candidates.get(0);
                Map<String, Object> content = (Map<String, Object>) firstCandidate.get("content");
                List<Map<String, Object>> parts = (List<Map<String, Object>>) content.get("parts");
                if (parts != null && !parts.isEmpty()) {
                    String description = (String) parts.get(0).get("text");
                    return description != null ? description : "No se pudo generar una descripción";
                }
            }
            return "No se pudo generar una descripción";
        } catch (Exception e) {
            System.err.println("❌ Error extrayendo descripción: " + e.getMessage());
            e.printStackTrace();
            return "Error al procesar la respuesta de Gemini";
        }
    }

    public String getModelName() {
        return MODEL_NAME;
    }
}