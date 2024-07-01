package apap.tutorial.bacabaca.dto.request;

import apap.tutorial.bacabaca.model.Penerbit;
import apap.tutorial.bacabaca.model.Penulis;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UpdateBukuRequestDTO extends CreateBukuRequestDTO {

    private UUID id;
    
    private List<Penulis> listPenulis;

    private String sourceLanguage; // Tambahkan properti sourceLanguage

    private String targetLanguage; // Tambahkan properti targetLanguage
}
