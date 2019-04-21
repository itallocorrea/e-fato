package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Unidade;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UnidadeRepository extends CrudRepository<Unidade, String>  {
    Unidade findByCodigo(long codigo);
    Unidade findByDescricao(String login);
}
