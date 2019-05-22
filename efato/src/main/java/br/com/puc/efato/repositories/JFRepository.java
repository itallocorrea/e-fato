package br.com.puc.efato.repositories;

import br.com.puc.efato.models.db.JF;
import br.com.puc.efato.models.db.Status;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface JFRepository extends CrudRepository<JF, String>  {
    JF findByCodigo(long jf_codigo);

    @Query("SELECT j FROM jf j WHERE turma_codigo = ?1 ")
    List<JF> findByFilterTurma(long turma_codigo);

    @Query("SELECT j FROM jf j WHERE turma_codigo = ?1 and status_codigo = ?2 ")
    List<JF> findByFilterTurmaAndStatus(long turma_codigo,long status_codigo);

    List<JF> findByStatus(Status status);

    @Transactional
    @Modifying
    void deleteByCodigo(long codigo);
}
