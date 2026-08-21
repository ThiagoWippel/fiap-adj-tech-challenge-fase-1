package br.com.fiap.restaurante.repositories;

import br.com.fiap.restaurante.entities.Usuario;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Acesso a dados de usuarios, independente do subtipo.
 *
 * Por operar sobre a raiz da hierarquia, as consultas aqui alcancam clientes
 * e donos de restaurante indistintamente - que e exatamente o comportamento
 * desejado para busca por nome, autenticacao e verificacao de unicidade.
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    /**
     * Busca por nome parcial, sem diferenciar maiusculas de minusculas.
     *
     * Nao ha indice sobre a coluna "nome" de proposito: a consulta gerada
     * procura o termo em qualquer posicao, e indices de arvore so sao
     * aproveitados em buscas por prefixo. Criar o indice daria a impressao
     * de otimizacao sem produzir nenhuma.
     */
    Page<Usuario> findByNomeContainingIgnoreCase(String nome, Pageable paginacao);

    /**
     * Mesma consulta, sem paginacao. Atende a versao 1 da busca, que devolve
     * uma lista simples. O Spring Data distingue as duas pelo tipo de retorno.
     */
    List<Usuario> findByNomeContainingIgnoreCase(String nome);

    /**
     * Usado pelo servico de autenticacao.
     */
    Optional<Usuario> findByLogin(String login);

    // --- Verificacoes de unicidade no cadastro ---

    boolean existsByEmail(String email);

    boolean existsByLogin(String login);

    // --- Verificacoes de unicidade na atualizacao ---
    //
    // A pergunta correta e "existe OUTRO usuario com este e-mail?", e nao
    // "existe algum usuario com este e-mail?". Sem excluir o proprio registro
    // da busca, salvar um cadastro sem alterar o e-mail resultaria em conflito
    // do usuario consigo mesmo.

    boolean existsByEmailAndIdNot(String email, Long id);

    boolean existsByLoginAndIdNot(String login, Long id);
}
