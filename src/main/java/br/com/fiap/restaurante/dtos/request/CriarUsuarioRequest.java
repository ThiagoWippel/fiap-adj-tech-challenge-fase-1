package br.com.fiap.restaurante.dtos.request;

import br.com.fiap.restaurante.entities.TipoUsuario;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.br.CNPJ;
import org.hibernate.validator.constraints.br.CPF;

/**
 * Dados de entrada para cadastro de usuario.
 *
 * As anotacoes de validacao cuidam da estrutura: obrigatoriedade, formato e
 * tamanho de cada campo. A regra de qual documento e exigido para cada tipo
 * de usuario e de negocio, e por isso vive no servico - onde tambem se resolve
 * a unicidade, que depende de consulta ao banco.
 */
@Schema(description = "Dados para cadastro de um novo usuario")
public record CriarUsuarioRequest(

        @Schema(example = "Maria Silva")
        @NotBlank(message = "O nome e obrigatorio")
        @Size(min = 3, max = 120, message = "O nome deve ter entre 3 e 120 caracteres")
        String nome,

        @Schema(example = "maria.silva@exemplo.com")
        @NotBlank(message = "O e-mail e obrigatorio")
        @Email(message = "O e-mail informado nao e valido")
        @Size(max = 255, message = "O e-mail deve ter no maximo 255 caracteres")
        String email,

        @Schema(example = "maria.silva")
        @NotBlank(message = "O login e obrigatorio")
        @Size(min = 4, max = 50, message = "O login deve ter entre 4 e 50 caracteres")
        @Pattern(
                regexp = "^[A-Za-z0-9._-]+$",
                message = "O login aceita apenas letras, numeros, ponto, hifen e sublinhado"
        )
        String login,

        // O limite superior nao e arbitrario: o BCrypt processa no maximo 72
        // bytes e descarta silenciosamente o excedente. Sem esta validacao,
        // uma senha mais longa permitiria autenticacao usando apenas o inicio.
        @Schema(example = "SenhaSegura123")
        @NotBlank(message = "A senha e obrigatoria")
        @Size(min = 8, max = 72, message = "A senha deve ter entre 8 e 72 caracteres")
        String senha,

        @Schema(example = "CLIENTE", allowableValues = {"CLIENTE", "DONO_RESTAURANTE"})
        @NotNull(message = "O tipo de usuario e obrigatorio")
        TipoUsuario tipo,

        // Validado apenas quando presente. A exigencia por tipo e verificada
        // no servico, junto com a unicidade.
        @Schema(example = "12345678909", description = "Obrigatorio quando o tipo e CLIENTE")
        @CPF(message = "O CPF informado nao e valido")
        String cpf,

        @Schema(example = "11222333000181", description = "Obrigatorio quando o tipo e DONO_RESTAURANTE")
        @CNPJ(message = "O CNPJ informado nao e valido")
        String cnpj,

        // @Valid propaga a validacao para dentro do objeto aninhado. Sem ele,
        // os campos do endereco nao seriam verificados.
        @NotNull(message = "O endereco e obrigatorio")
        @Valid
        EnderecoRequest endereco
) {
}
