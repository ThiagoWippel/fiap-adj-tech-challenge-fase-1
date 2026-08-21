package br.com.fiap.restaurante.repositories;

import br.com.fiap.restaurante.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Consultas especificas de cliente.
 *
 * Existe separado de UsuarioRepository por uma razao concreta: o CPF e um
 * atributo de Cliente, nao de Usuario. Um metodo derivado nao pode referenciar
 * um campo ausente na entidade sobre a qual o repositorio opera.
 *
 * O resultado atende ao Principio da Segregacao de Interfaces: quem so precisa
 * autenticar um usuario nao passa a depender de metodos sobre CPF.
 */
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    boolean existsByCpf(String cpf);

    boolean existsByCpfAndIdNot(String cpf, Long id);
}
