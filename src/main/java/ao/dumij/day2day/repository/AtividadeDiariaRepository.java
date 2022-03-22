package ao.dumij.day2day.repository;

import ao.dumij.day2day.models.AtividadeDiaria;
//import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface AtividadeDiariaRepository extends CrudRepository<AtividadeDiaria, Long> {
    List<AtividadeDiaria> findByDataAfter(Date data);

    List<AtividadeDiaria> findByDataBefore(Date data);

    List<AtividadeDiaria> findByData(Date data);

    AtividadeDiaria findAtividadeDiariaById(long id);

    void deleteById(long id);
}
