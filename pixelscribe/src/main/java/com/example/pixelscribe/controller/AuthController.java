package com.example.pixelscribe.controller;

import com.example.pixelscribe.model.entities.User;
import com.example.pixelscribe.services.MatcherService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@Tag(name = "Autenticación", description = "Endpoints para registrar e iniciar sesión en PixelScribe")
public class AuthController {

    private final MatcherService matcherService;

    // ------------------ REGISTER ------------------

    @Operation(
            summary = "Registrar un nuevo usuario",
            description = "Crea un nuevo usuario en el sistema a partir de su correo electrónico y contraseña.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo de registro",
                                            value = "{ \"email\": \"usuario@example.com\", \"password\": \"miContraseñaSegura123\" }"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Usuario registrado correctamente",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{ \"userId\": \"6728c54a99f3b12f84e1a2c3\", \"email\": \"usuario@example.com\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en el registro (usuario ya existente o datos inválidos)",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{ \"error\": \"El correo ya está registrado\" }"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/register")
    public ResponseEntity<?> register(@org.springframework.web.bind.annotation.RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            User newUser = matcherService.register(email, password);
            return ResponseEntity.ok(Map.of(
                    "userId", newUser.getUserId(),
                    "email", newUser.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    // ------------------ LOGIN ------------------

    @Operation(
            summary = "Iniciar sesión de usuario",
            description = "Autentica a un usuario existente y devuelve su información básica.",
            requestBody = @RequestBody(
                    required = true,
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "Ejemplo de login",
                                            value = "{ \"email\": \"usuario@example.com\", \"password\": \"miContraseñaSegura123\" }"
                                    )
                            }
                    )
            ),
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Inicio de sesión exitoso",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{ \"userId\": \"6728c54a99f3b12f84e1a2c3\", \"email\": \"usuario@example.com\" }"
                                    )
                            )
                    ),
                    @ApiResponse(
                            responseCode = "400",
                            description = "Error en las credenciales (usuario no encontrado o contraseña incorrecta)",
                            content = @Content(
                                    mediaType = "application/json",
                                    examples = @ExampleObject(
                                            value = "{ \"error\": \"Contraseña incorrecta\" }"
                                    )
                            )
                    )
            }
    )
    @PostMapping("/login")
    public ResponseEntity<?> login(@org.springframework.web.bind.annotation.RequestBody Map<String, String> body) {
        try {
            String email = body.get("email");
            String password = body.get("password");
            User user = matcherService.authenticate(email, password);
            return ResponseEntity.ok(Map.of(
                    "userId", user.getUserId(),
                    "email", user.getEmail()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}
