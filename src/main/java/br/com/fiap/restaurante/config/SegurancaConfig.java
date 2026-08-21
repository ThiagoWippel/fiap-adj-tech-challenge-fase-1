package br.com.fiap.restaurante.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Codificacao de senhas.
 *
 * Usa apenas spring-security-crypto, a biblioteca de criptografia isolada, sem
 * a cadeia de filtros do Spring Security completo - que o enunciado dispensa
 * explicitamente.
 *
 * O BCrypt e unidirecional: o valor armazenado nao pode ser revertido. A
 * verificacao compara hashes, nunca textos. O algoritmo incorpora um valor
 * aleatorio por registro, de modo que senhas identicas produzem hashes
 * diferentes - impedindo que senhas repetidas sejam identificadas por
 * inspecao do banco.
 *
 * O tipo devolvido e a interface PasswordEncoder, e nao a implementacao. Os
 * servicos passam a depender da abstracao, o que atende ao Principio da
 * Inversao de Dependencia e permite substituir o algoritmo alterando apenas
 * esta classe.
 */
@Configuration
public class SegurancaConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
