package br.com.fiap.restaurante.controllers.handlers;

import br.com.fiap.restaurante.dtos.response.ErroDeCampo;
import br.com.fiap.restaurante.services.exceptions.ConflitoDeDadosException;
import br.com.fiap.restaurante.services.exceptions.CredenciaisInvalidasException;
import br.com.fiap.restaurante.services.exceptions.RecursoNaoEncontradoException;
import br.com.fiap.restaurante.services.exceptions.RegraDeNegocioException;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Comparator;
import java.util.List;

/**
 * Tratamento centralizado de erros, no formato ProblemDetail (RFC 7807).
 *
 * Concentrar o tratamento em um unico ponto mantem os controllers limpos e
 * garante que toda falha saia no mesmo formato - inclusive as que nenhum
 * controller previu.
 *
 * A RFC define cinco campos: type, title, status, detail e instance. A
 * distincao entre title e detail costuma confundir: title e constante para
 * aquele tipo de problema, detail descreve a ocorrencia especifica.
 * Acrescentamos duas extensoes: "momento", que permite correlacionar a resposta
 * com o registro em log, e "erros", com o detalhamento por campo nas falhas de
 * validacao.
 */
@RestControllerAdvice
// Precedencia maxima. Com spring.mvc.problemdetails.enabled=true, o proprio
// Spring Boot registra um tratador para as excecoes do framework - incluindo a
// de validacao. Sem declarar a ordem, os dois disputam MethodArgumentNotValidException
// e o do Spring vence, devolvendo uma mensagem generica sem a relacao de campos
// rejeitados. Com a precedencia definida, o nosso atende primeiro e o do Spring
// permanece cobrindo o que nao declaramos aqui: JSON malformado, verbo nao
// suportado, parametro ausente.
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ControllerExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(ControllerExceptionHandler.class);

    /**
     * Base dos identificadores de tipo de problema.
     *
     * O comportamento padrao do Spring preenche "type" com o valor generico
     * "about:blank", que nao identifica nada. Apontar para ancoras reais na
     * documentacao do projeto e o que diferencia usar a classe ProblemDetail de
     * compreender o proposito da RFC: o campo existe para que um cliente possa
     * reconhecer programaticamente a categoria do erro e consultar sua
     * descricao.
     */
    private static final String BASE_TIPOS =
            "https://github.com/ThiagoWippel/fiap-adj-tech-challenge-fase-1/blob/main/README.md";

    // ------------------------------------------------------------------
    // 404 - Recurso inexistente
    // ------------------------------------------------------------------

    @ExceptionHandler(RecursoNaoEncontradoException.class)
    public ProblemDetail tratarRecursoNaoEncontrado(RecursoNaoEncontradoException excecao,
                                                    HttpServletRequest requisicao) {
        return montar(
                HttpStatus.NOT_FOUND,
                "Recurso nao encontrado",
                excecao.getMessage(),
                "#recurso-nao-encontrado",
                requisicao
        );
    }

    // ------------------------------------------------------------------
    // 409 - Conflito com o estado atual
    // ------------------------------------------------------------------

    @ExceptionHandler(ConflitoDeDadosException.class)
    public ProblemDetail tratarConflito(ConflitoDeDadosException excecao,
                                        HttpServletRequest requisicao) {
        return montar(
                HttpStatus.CONFLICT,
                "Conflito de dados",
                excecao.getMessage(),
                "#conflito-de-dados",
                requisicao
        );
    }

    /**
     * Rede de seguranca para violacoes de unicidade detectadas pelo banco.
     *
     * O servico verifica a unicidade antes de gravar, mas duas requisicoes
     * simultaneas podem passar por essa verificacao e colidir na escrita. A
     * restricao do banco e o unico mecanismo capaz de resolver essa disputa;
     * este tratamento converte a falha resultante em uma resposta coerente.
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ProblemDetail tratarViolacaoDeIntegridade(DataIntegrityViolationException excecao,
                                                     HttpServletRequest requisicao) {
        logger.warn("Violacao de integridade no banco: {}", excecao.getMostSpecificCause().getMessage());
        return montar(
                HttpStatus.CONFLICT,
                "Conflito de dados",
                "Os dados informados violam uma restricao de unicidade do sistema",
                "#conflito-de-dados",
                requisicao
        );
    }

    // ------------------------------------------------------------------
    // 401 - Credenciais invalidas
    // ------------------------------------------------------------------

    @ExceptionHandler(CredenciaisInvalidasException.class)
    public ProblemDetail tratarCredenciaisInvalidas(CredenciaisInvalidasException excecao,
                                                    HttpServletRequest requisicao) {
        return montar(
                HttpStatus.UNAUTHORIZED,
                "Credenciais invalidas",
                excecao.getMessage(),
                "#credenciais-invalidas",
                requisicao
        );
    }

    // ------------------------------------------------------------------
    // 400 - Regra de negocio
    // ------------------------------------------------------------------

    @ExceptionHandler(RegraDeNegocioException.class)
    public ProblemDetail tratarRegraDeNegocio(RegraDeNegocioException excecao,
                                              HttpServletRequest requisicao) {
        return montar(
                HttpStatus.BAD_REQUEST,
                "Regra de negocio violada",
                excecao.getMessage(),
                "#regra-de-negocio",
                requisicao
        );
    }

    // ------------------------------------------------------------------
    // 400 - Validacao de campos
    // ------------------------------------------------------------------

    /**
     * Falhas das anotacoes de validacao dos DTOs.
     *
     * Declarar este tratamento sobrepoe o comportamento padrao do Spring, que
     * responderia apenas com uma mensagem generica. Aqui a resposta carrega a
     * relacao completa de campos rejeitados.
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail tratarValidacao(MethodArgumentNotValidException excecao,
                                         HttpServletRequest requisicao) {

        List<ErroDeCampo> erros = excecao.getBindingResult().getFieldErrors().stream()
                .map(erro -> new ErroDeCampo(erro.getField(), erro.getDefaultMessage()))
                .sorted(Comparator.comparing(ErroDeCampo::campo))
                .toList();

        logger.warn("Requisicao rejeitada por validacao: {} campo(s) invalido(s)", erros.size());

        ProblemDetail problema = montar(
                HttpStatus.BAD_REQUEST,
                "Dados invalidos",
                "Um ou mais campos da requisicao nao passaram na validacao",
                "#dados-invalidos",
                requisicao
        );
        problema.setProperty("erros", erros);
        return problema;
    }

    // ------------------------------------------------------------------
    // 500 - Falha nao prevista
    // ------------------------------------------------------------------

    /**
     * Ultima barreira.
     *
     * O rastro da excecao vai para o log, nunca para a resposta: mensagens
     * internas revelam estrutura de pacotes, versoes de biblioteca e por vezes
     * trechos de consulta ao banco. O cliente recebe apenas uma mensagem
     * generica, conforme orienta o material de Excecoes da disciplina.
     */
    @ExceptionHandler(Exception.class)
    public ProblemDetail tratarFalhaNaoPrevista(Exception excecao,
                                                HttpServletRequest requisicao) {
        logger.error("Falha nao prevista ao processar {}", requisicao.getRequestURI(), excecao);
        return montar(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Erro interno",
                "Ocorreu uma falha inesperada ao processar a requisicao",
                "#erro-interno",
                requisicao
        );
    }

    // ------------------------------------------------------------------
    // Apoio
    // ------------------------------------------------------------------

    private ProblemDetail montar(HttpStatus status, String titulo, String detalhe,
                                 String ancora, HttpServletRequest requisicao) {

        ProblemDetail problema = ProblemDetail.forStatusAndDetail(status, detalhe);
        problema.setTitle(titulo);
        problema.setType(URI.create(BASE_TIPOS + ancora));
        problema.setInstance(URI.create(requisicao.getRequestURI()));
        problema.setProperty("momento", LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).toString());
        return problema;
    }
}