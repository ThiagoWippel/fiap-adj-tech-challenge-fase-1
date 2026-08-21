package br.com.fiap.restaurante.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 * Habilita a auditoria automatica de datas do Spring Data JPA.
 *
 * Sem esta configuracao, as anotacoes @CreatedDate e @LastModifiedDate nas
 * entidades sao simplesmente ignoradas - sem erro, sem aviso. A data ficaria
 * nula e a insercao falharia por violacao de NOT NULL.
 *
 * Atende ao requisito de "registro da data da ultima alteracao" sem exigir
 * que cada operacao de escrita lembre de atualizar o campo manualmente.
 */
@Configuration
@EnableJpaAuditing
public class JpaAuditingConfig {
}
