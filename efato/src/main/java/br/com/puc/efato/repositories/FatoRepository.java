package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.Fato;
import br.com.puc.efato.models.db.JF;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface FatoRepository extends CrudRepository<Fato, String>  {
    Fato findByCodigo(int codigo);

    List<Fato> findByJf(JF jf);

    @Transactional
    @Modifying
    @Query("DELETE FROM fato f WHERE f.codigo IN (:codigos)")
    void deleteInIdList(@Param("codigos") List<Long> codigos);
}