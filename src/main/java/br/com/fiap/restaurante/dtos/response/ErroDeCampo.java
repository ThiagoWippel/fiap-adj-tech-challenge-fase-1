package br.com.fiap.restaurante.dtos.response;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Detalhe de uma falha de validacao em um campo especifico.
 *
 * A RFC 7807 define cinco campos padrao e permite extensoes. Este objeto compoe
 * a extensao "erros" da resposta: sem ele, uma requisicao com tres campos
 * invalidos teria de espremer tudo em uma unica frase, e o cliente nao
 * conseguiria destacar quais campos corrigir.
 */
@Schema(description = "Falha de validacao em um campo")
public record ErroDeCampo(

        @Schema(example = "email")
        String campo,

        @Schema(example = "O e-mail informado nao e valido")
        String mensagem
) {
}
