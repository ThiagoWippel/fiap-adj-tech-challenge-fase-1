package br.com.fiap.restaurante.dtos.response;

import br.com.fiap.restaurante.entities.TipoUsuario;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

/**
 * Representacao publica de um usuario.
 *
 * A senha simplesmente nao existe aqui. Nao ha campo a ocultar, nem anotacao a
 * lembrar de aplicar - a estrutura torna o vazamento impossivel por construcao,
 * que e a razao de a API nao expor a entidade diretamente.
 *
 * Note a assimetria em relacao ao dominio: la ha heranca, porque o polimorfismo
 * tem valor no comportamento. Aqui ha uma estrutura unica, porque a
 * representacao externa dos dois tipos e identica. Modelo interno e contrato
 * externo nao precisam ter a mesma forma.
 */
@Schema(description = "Dados publicos de um usuario")
public record UsuarioResponse(

        @Schema(example = "1")
        Long id,

        @Schema(example = "Maria Silva")
        String nome,

        @Schema(example = "maria.silva@exemplo.com")
        String email,

        @Schema(example = "maria.silva")
        String login,

        @Schema(example = "CLIENTE")
        TipoUsuario tipo,

        // Campo unico em vez de cpf e cnpj separados: um usuario possui
        // exatamente um documento, determinado pelo tipo. Dois campos deixariam
        // um deles permanentemente nulo em toda resposta.
        @Schema(example = "12345678909", description = "CPF para cliente, CNPJ para dono de restaurante")
        String documento,

        EnderecoResponse endereco,

        // Sem o formato explicito, o Jackson serializa LocalDateTime com
        // precisao de nanossegundos, enquanto a coluna no banco guarda
        // microssegundos. O objeto recem-criado sairia com nove casas decimais e
        // o mesmo registro, relido do MySQL, com seis - divergencia que
        // apareceria nos prints da documentacao.
        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(example = "2026-08-21T10:30:00")
        LocalDateTime dataCriacao,

        @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
        @Schema(example = "2026-08-21T14:45:00")
        LocalDateTime dataUltimaAlteracao
) {
}
