package br.com.fiap.restaurante.dtos.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Endereco do usuario")
public record EnderecoRequest(

        @Schema(example = "Rua das Flores")
        @NotBlank(message = "A rua e obrigatoria")
        @Size(max = 150, message = "A rua deve ter no maximo 150 caracteres")
        String rua,

        @Schema(example = "123", description = "Aceita valores como S/N e 123-A")
        @NotBlank(message = "O numero e obrigatorio")
        @Size(max = 10, message = "O numero deve ter no maximo 10 caracteres")
        String numero,

        @Schema(example = "Apto 45")
        @Size(max = 60, message = "O complemento deve ter no maximo 60 caracteres")
        String complemento,

        @Schema(example = "Centro")
        @NotBlank(message = "O bairro e obrigatorio")
        @Size(max = 80, message = "O bairro deve ter no maximo 80 caracteres")
        String bairro,

        @Schema(example = "Camboriu")
        @NotBlank(message = "A cidade e obrigatoria")
        @Size(max = 80, message = "A cidade deve ter no maximo 80 caracteres")
        String cidade,

        @Schema(example = "SC")
        @NotBlank(message = "O estado e obrigatorio")
        @Pattern(regexp = "^[A-Za-z]{2}$", message = "O estado deve conter exatamente duas letras")
        String estado,

        @Schema(example = "88340-000", description = "Aceita com ou sem pontuacao")
        @NotBlank(message = "O CEP e obrigatorio")
        @Pattern(regexp = "^\\d{5}-?\\d{3}$", message = "O CEP deve conter oito digitos")
        String cep
) {
}
