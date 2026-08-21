package br.com.fiap.restaurante.services.exceptions;

/**
 * A requisicao e valida, mas conflita com o estado atual do banco.
 * Mapeada para HTTP 409.
 *
 * Usada para violacoes de unicidade: e-mail, login, CPF ou CNPJ ja cadastrados.
 */
public class ConflitoDeDadosException extends RuntimeException {

    public ConflitoDeDadosException(String mensagem) {
        super(mensagem);
    }
}
