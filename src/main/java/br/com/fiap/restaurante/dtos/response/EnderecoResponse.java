package br.com.fiap.restaurante.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Endereco do usuario")
public record EnderecoResponse(
        String rua,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        String cep
) {
}
