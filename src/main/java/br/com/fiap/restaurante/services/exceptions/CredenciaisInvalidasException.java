package br.com.fiap.restaurante.services.exceptions;

/**
 * Credenciais nao conferem. Mapeada para HTTP 401.
 *
 * A mensagem e deliberadamente generica e identica tanto para login inexistente
 * quanto para senha incorreta. Diferencia-las permitiria descobrir quais logins
 * existem no sistema por tentativa e erro.
 */
public class CredenciaisInvalidasException extends RuntimeException {

    public CredenciaisInvalidasException(String mensagem) {
        super(mensagem);
    }
}
