package br.com.fiap.restaurante.repositories;

import br.com.fiap.restaurante.entities.DonoRestaurante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Consultas especificas de dono de restaurante.
 *
 * Mesma justificativa do ClienteRepository: o CNPJ pertence a esta subclasse.
 */
@Repository
public interface DonoRestauranteRepository extends JpaRepository<DonoRestaurante, Long> {

    boolean existsByCnpj(String cnpj);

    boolean existsByCnpjAndIdNot(String cnpj, Long id);
}
