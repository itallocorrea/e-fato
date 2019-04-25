package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Topico;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends CrudRepository<Topico, String>  {
    Topico findByCodigo(long topico_codigo);
}
