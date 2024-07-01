package apap.tutorial.bacabaca.dto;

import apap.tutorial.bacabaca.dto.request.CreateBukuRequestDTO;
import apap.tutorial.bacabaca.dto.request.UpdateBukuRequestDTO;
import apap.tutorial.bacabaca.dto.response.ReadBukuResponseDTO;
import apap.tutorial.bacabaca.model.Buku;
//import net.bytebuddy.asm.MemberSubstitution.Substitution.Chain.Step.ForField.Read;

import org.mapstruct.AfterMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "spring")
public interface BukuMapper{

    Buku createBukuRequestDTOToBuku(CreateBukuRequestDTO createBukuRequestDTO);
    // @AfterMapping
    // default void judulLowerCreate(CreateBukuRequestDTO createBukuRequestDTO, @MappingTarget Buku buku){
    //     buku.setJudulLower(createBukuRequestDTO.getJudul().toLowerCase());
    // }

    Buku updateBukuRequestDTOToBuku(UpdateBukuRequestDTO updateBukuRequestDTO);

    UpdateBukuRequestDTO bukuToUpdateBukuRequestDTO(Buku buku);

    ReadBukuResponseDTO readBukuResponseDTO(Buku buku);

    @AfterMapping
    default void setNamaPenerbit(@MappingTarget ReadBukuResponseDTO bukuDTO, Buku buku) {
        // Ambil nilai nama_penerbit dari objek buku
        String namaPenerbit = buku.getPenerbit().getNamaPenerbit();
        
        // Setel nilai nama_penerbit di dalam objek bukuDTO
        bukuDTO.setNamaPenerbit(namaPenerbit);
    }


    ReadBukuResponseDTO readValue(String responseBody, Class<ReadBukuResponseDTO> readBukuResponseDTOClass);

    Buku readBukuResponseDTOToBuku(ReadBukuResponseDTO readBukuResponseDTO);
}