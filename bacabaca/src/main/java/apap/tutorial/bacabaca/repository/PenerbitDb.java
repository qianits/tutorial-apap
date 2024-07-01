package apap.tutorial.bacabaca.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import apap.tutorial.bacabaca.model.Penerbit;

@Repository
public interface PenerbitDb extends JpaRepository<Penerbit, Long>{
    List<Penerbit> findAll();
    Optional<Penerbit> findById(Long idPenerbit);
}
