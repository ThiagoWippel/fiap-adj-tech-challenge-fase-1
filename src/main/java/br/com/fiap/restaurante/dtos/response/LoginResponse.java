package br.com.fiap.restaurante.dtos.response;

import br.com.fiap.restaurante.entities.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Resposta da validacao de login.
 *
 * Devolve apenas o minimo para identificar quem autenticou. Nao e uma consulta
 * de perfil: e-mail e endereco ficam de fora porque a operacao responde "estas
 * credenciais conferem?", nao "quem e este usuario?".
 */
@Schema(description = "Resultado da validacao de credenciais")
public record LoginResponse(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Maria Silva")
        String nome,

        @Schema(example = "CLIENTE")
        TipoUsuario tipo
) {
}
