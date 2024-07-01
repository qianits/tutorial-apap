package apap.tutorial.bacabaca.dto;

import apap.tutorial.bacabaca.controller.BukuController;
import apap.tutorial.bacabaca.controller.PenerbitController;
import apap.tutorial.bacabaca.dto.request.CreatePenerbitRequestDTO;
import apap.tutorial.bacabaca.dto.request.UpdatePenerbitRequestDTO;
import apap.tutorial.bacabaca.model.Penerbit;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PenerbitMapper {
    Penerbit createPenerbitRequestDTOToPenerbit(CreatePenerbitRequestDTO createPenerbitRequestDTO);

    Penerbit updatePenerbitRequestDTOToPenerbit(UpdatePenerbitRequestDTO updatePenerbitRequestDTO);

}
