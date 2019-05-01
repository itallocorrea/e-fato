package br.com.puc.efato.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import br.com.puc.efato.models.db.Equipe;
import br.com.puc.efato.models.db.JF;

@Repository
public interface EquipeRepository extends CrudRepository<Equipe, Long>{

	List<Equipe> findByJf(JF jf);
	Equipe findByCodigo(long codigo);
}
