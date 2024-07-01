package apap.tutorial.bacabaca.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "buku")
@SQLDelete(sql = "UPDATE buku SET is_deleted = true WHERE id=?")
@Where(clause = "is_deleted=false")
@JsonIgnoreProperties(value={"penerbit"}, allowSetters = true)
public class Buku {

    @Id
    private UUID id = UUID.randomUUID();

//    @NotNull
    @Size(max = 100)
    @Column(name = "judul", nullable = true)
    private String judul;

    @NotNull
    @Size(max = 4)
    @Column(name = "tahun terbit", nullable = false)
    private String tahunTerbit;

    @NotNull
    @Column(name = "harga", nullable = false)
    private BigDecimal harga;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_penerbit", referencedColumnName = "idPenerbit")
    private Penerbit penerbit;

    @NotNull
    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;

    @Column(name = "source_language", nullable = true)
    private String sourceLanguage;

    @Column(name = "target_language", nullable = true)
    private String targetLanguage;

    @Column(name = "judul_lower", nullable = true)
    private String judulLower;

    @ManyToMany
    @JoinTable(name = "penulis_buku", joinColumns = @JoinColumn(name = "id"),
    inverseJoinColumns = @JoinColumn(name = "id_penulis"))
    List<Penulis> listPenulis;

}
