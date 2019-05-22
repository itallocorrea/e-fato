package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Equipe;
import br.com.puc.efato.models.db.JF;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipeRepository extends CrudRepository<Equipe, Long>{

	List<Equipe> findByJf(JF jf);
	Equipe findByCodigo(long codigo);
}
