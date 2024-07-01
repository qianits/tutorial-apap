package apap.tutorial.bacabaca.dto.request;

import apap.tutorial.bacabaca.model.Penerbit;
import apap.tutorial.bacabaca.model.Penulis;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CreateBukuRequestDTO{
    @NotBlank(message = "Judul tidak boleh kosong")
    @Size(max = 255, message = "Judul tidak boleh lebih dari 255 karakter")
    private String judul;

    @NotBlank(message = "Tahun terbit tidak boleh kosong")
    @Pattern(regexp = "\\d{4}", message = "Tahun terbit harus berupa angka 4 digit")
    private String tahunTerbit;

    @NotNull()
    @DecimalMin(value = "1000", inclusive = false, message = "Harga harus minimal 1000")
    private BigDecimal harga;

    @NotNull()
    private Penerbit penerbit;

    private boolean isDeleted;

    private String judulLower;

    private List<Penulis> listPenulis;

    private String sourceLanguage;
    private String targetLanguage;

}