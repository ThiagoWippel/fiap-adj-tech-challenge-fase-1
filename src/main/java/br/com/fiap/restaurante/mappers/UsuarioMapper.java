package br.com.fiap.restaurante.mappers;

import br.com.fiap.restaurante.dtos.request.EnderecoRequest;
import br.com.fiap.restaurante.dtos.response.EnderecoResponse;
import br.com.fiap.restaurante.dtos.response.LoginResponse;
import br.com.fiap.restaurante.dtos.response.UsuarioResponse;
import br.com.fiap.restaurante.entities.Endereco;
import br.com.fiap.restaurante.entities.Usuario;
import org.springframework.stereotype.Component;

/**
 * Conversao entre entidades e objetos de transferencia.
 *
 * Escrito a mao, sem biblioteca de mapeamento automatico. Bibliotecas do genero
 * associam campos por reflexao em tempo de execucao: um nome errado nao impede
 * a compilacao e so se manifesta quando a aplicacao roda. Com poucas conversoes,
 * o mapeamento explicito e mais rapido de depurar e torna visivel, no proprio
 * codigo, que a senha nao atravessa para a resposta.
 *
 * Note que getDocumento() dispensa qualquer teste de tipo: a entidade sabe
 * responder qual e o seu documento.
 */
@Component
public class UsuarioMapper {

    public UsuarioResponse paraResposta(Usuario usuario) {
        return new UsuarioResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getLogin(),
                usuario.getTipo(),
                usuario.getDocumento(),
                paraEnderecoResposta(usuario.getEndereco()),
                usuario.getDataCriacao(),
                usuario.getDataUltimaAlteracao()
        );
    }

    public LoginResponse paraRespostaLogin(Usuario usuario) {
        return new LoginResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getTipo()
        );
    }

    public Endereco paraEntidade(EnderecoRequest requisicao, String cepNormalizado) {
        return new Endereco(
                requisicao.rua(),
                requisicao.numero(),
                requisicao.complemento(),
                requisicao.bairro(),
                requisicao.cidade(),
                requisicao.estado().toUpperCase(),
                cepNormalizado
        );
    }

    private EnderecoResponse paraEnderecoResposta(Endereco endereco) {
        if (endereco == null) {
            return null;
        }
        return new EnderecoResponse(
                endereco.getRua(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep()
        );
    }
}
