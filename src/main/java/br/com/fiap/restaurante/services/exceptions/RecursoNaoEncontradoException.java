package br.com.fiap.restaurante.services.exceptions;

/**
 * Recurso solicitado nao existe. Mapeada para HTTP 404.
 *
 * Estende RuntimeException por opcao deliberada: a ausencia de um registro nao
 * e condicao que o chamador possa tratar e prosseguir. Obrigar cada camada
 * intermediaria a declarar ou capturar so acrescentaria ruido.
 */
public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String mensagem) {
        super(mensagem);
    }
}
