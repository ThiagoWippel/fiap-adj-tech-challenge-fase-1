package br.com.fiap.restaurante.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

/**
 * Dados de entrada para atualizacao de usuario.
 *
 * Nao aceita senha - a troca tem endpoint proprio, conforme o enunciado.
 * Nao aceita tipo nem documento: sao imutaveis apos o cadastro. O que
 * identifica quem a pessoa e permanece fixo; o que e dado de contato e
 * editavel.
 */
@Schema(description = "Dados para atualizacao de um usuario existente")
public record AtualizarUsuarioRequest(

        @Schema(example = "Maria Silva Souza")
        @NotBlank(message = "O nome e obrigatorio")
        @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
        String nome,

        @Schema(example = "maria.souza@exemplo.com")
        @NotBlank(message = "O e-mail e obrigatorio")
        @Email(message = "O e-mail informado nao e valido")
        @Size(max = 255, message = "O e-mail deve ter no maximo 255 caracteres")
        String email,

        @Schema(example = "maria.souza")
        @NotBlank(message = "O login e obrigatorio")
        @Size(min = 4, max = 50, message = "O login deve ter entre 4 e 50 caracteres")
        @Pattern(
                regexp = "^[A-Za-z0-9._-]+$",
                message = "O login aceita apenas letras, numeros, ponto, hifen e sublinhado"
        )
        String login,

        @NotNull(message = "O endereco e obrigatorio")
        @Valid
        EnderecoRequest endereco
) {
}
