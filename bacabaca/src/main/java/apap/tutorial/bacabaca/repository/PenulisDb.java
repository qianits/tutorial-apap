package apap.tutorial.bacabaca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import apap.tutorial.bacabaca.model.Penulis;
import jakarta.transaction.Transactional;

@Repository
@Transactional
public interface PenulisDb extends JpaRepository<Penulis, Long>{
    Integer deleteByIdPenulisIn(List<Long> listIdPenulis);
}
