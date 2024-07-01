package apap.tutorial.bacabaca.repository;

import apap.tutorial.bacabaca.model.Buku;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BukuDb extends JpaRepository<Buku, UUID> {
    // Derived Query Method untuk mencari buku berdasarkan judul yang mengandung teks (ignore case)
    List<Buku> findByJudulContainingIgnoreCase(String keyword);
    
    // Metode untuk melakukan sorting berdasarkan judul
    List<Buku> findAllByOrderByJudulLowerAsc(); // Urutkan secara ascending (A-Z)
    // Metode untuk melakukan sorting berdasarkan judul

    // List<Buku> sortBukuByJudulLower();

}