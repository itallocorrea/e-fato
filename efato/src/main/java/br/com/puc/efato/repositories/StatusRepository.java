package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Status;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StatusRepository extends CrudRepository<Status, String>  {
    Status findByCodigo(long codigo_status);
}
