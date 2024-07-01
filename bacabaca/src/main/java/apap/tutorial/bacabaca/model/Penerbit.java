package apap.tutorial.bacabaca.model;

import java.util.List;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "penerbit")
@SQLDelete(sql = "UPDATE penerbit SET is_deleted = true WHERE id_penerbit=?")
@Where(clause = "is_deleted=false")
@JsonIgnoreProperties(value={"buku"}, allowSetters = true)
public class Penerbit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idPenerbit;

    @NotNull
    @Column(name = "nama_penerbit", nullable = false)
    private String namaPenerbit;

    @NotNull
    @Column(name = "alamat", nullable = false)
    private String alamat;

    @NotNull
    @Column(name = "email", nullable = false)
    private String email;

    @OneToMany(mappedBy =  "penerbit", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<Buku> listBuku;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = Boolean.FALSE;
}
