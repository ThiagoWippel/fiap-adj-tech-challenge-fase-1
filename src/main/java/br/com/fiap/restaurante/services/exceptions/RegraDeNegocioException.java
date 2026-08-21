package br.com.fiap.restaurante.services.exceptions;

/**
 * Violacao de regra de negocio que nao e expressavel de forma declarativa nas
 * anotacoes de validacao. Mapeada para HTTP 400.
 *
 * Exemplo tipico: a exigencia de CPF para clientes e CNPJ para donos de
 * restaurante, que depende do valor de outro campo da mesma requisicao.
 */
public class RegraDeNegocioException extends RuntimeException {

    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
