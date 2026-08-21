package br.com.fiap.restaurante.entities;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Usuario que consome os servicos dos restaurantes.
 *
 * Identificado por CPF. A coluna aceita nulo no banco porque, na estrategia
 * de tabela unica, o registro de um dono de restaurante nao a preenche. A
 * obrigatoriedade por tipo e garantida na camada de aplicacao, pela validacao
 * condicional do DTO de cadastro.
 */
@Entity
@DiscriminatorValue("CLIENTE")
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Cliente extends Usuario {

    // Apenas digitos, sem pontuacao. Armazenar formatado permitiria que
    // "123.456.789-00" e "12345678900" coexistissem como registros distintos,
    // anulando na pratica a restricao de unicidade.
    @Column(name = "cpf", length = 11, unique = true)
    private String cpf;

    public Cliente(String nome, String email, String login, String senha,
                   Endereco endereco, String cpf) {
        super(nome, email, login, senha, endereco);
        this.cpf = cpf;
    }

    @Override
    public TipoUsuario getTipo() {
        return TipoUsuario.CLIENTE;
    }

    @Override
    public String getDocumento() {
        return this.cpf;
    }
}
