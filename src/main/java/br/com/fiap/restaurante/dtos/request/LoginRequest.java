package br.com.fiap.restaurante.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Credenciais para validacao de login")
public record LoginRequest(

        @Schema(example = "maria.silva")
        @NotBlank(message = "O login e obrigatorio")
        String login,

        @Schema(example = "SenhaSegura123")
        @NotBlank(message = "A senha e obrigatoria")
        String senha
) {
}
