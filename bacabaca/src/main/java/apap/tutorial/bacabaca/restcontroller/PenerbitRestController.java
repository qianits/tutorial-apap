package apap.tutorial.bacabaca.restcontroller;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import apap.tutorial.bacabaca.dto.PenerbitMapper;
import apap.tutorial.bacabaca.dto.request.CreateBukuRequestDTO;
import apap.tutorial.bacabaca.dto.request.CreatePenerbitRequestDTO;
import apap.tutorial.bacabaca.dto.request.UpdatePenerbitRequestDTO;
import apap.tutorial.bacabaca.model.Penerbit;
import apap.tutorial.bacabaca.restservice.PenerbitRestService;
import apap.tutorial.bacabaca.service.PenerbitService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class PenerbitRestController {
    
    @Autowired
    private PenerbitRestService penerbitRestService;

    @Autowired
    private PenerbitMapper penerbitMapper;

    @GetMapping(value ="/penerbit/view-all")
    private List<Penerbit> retrieveAllPenerbit(){
        return penerbitRestService.retrieveRestAllPenerbit();
    }

    @PostMapping(value = "/penerbit/create")
    private Penerbit createPenerbit(@Valid @RequestBody CreatePenerbitRequestDTO penerbitDTO, BindingResult bindingResult) {
        if(bindingResult.hasFieldErrors()){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Request body has invalid type or missin field"
            );
        } else{
            var penerbit = penerbitMapper.createPenerbitRequestDTOToPenerbit(penerbitDTO);
            return penerbitRestService.createRestPenerbit(penerbit);
        }
    }

    @PutMapping(value = "/penerbit/{idPenerbit}")
    private Penerbit putPenerbit(@PathVariable("idPenerbit") Long idPenerbit, @Valid @RequestBody UpdatePenerbitRequestDTO penerbitDTO){
        try {
            var penerbit = penerbitMapper.updatePenerbitRequestDTOToPenerbit(penerbitDTO);
            return penerbitRestService.putRestPenerbit(idPenerbit, penerbit);

        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Penerbit dengan ID" + String.valueOf(idPenerbit) + " Not Found!"
            );

        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Buku is still open!"
            );
        }
    }

    @GetMapping(value = "/penerbit/{idPenerbit}")
    private Penerbit getPenerbit(@PathVariable("idPenerbit") Long idPenerbit){
        try {
            return penerbitRestService.getRestPenerbitById(idPenerbit);
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Penerbit dengan ID" + String.valueOf(idPenerbit) + " Not Found!"
            );
        }
    }

    @DeleteMapping(value = "/penerbit/{idPenerbit}")
    private String deletePenerbit(@PathVariable("idPenerbit") Long idPenerbit){
        try {
            penerbitRestService.deletePenerbit(idPenerbit);;
            return "Penerbit has been deleted";
        } catch (NoSuchElementException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND,"Penerbit dengan ID" + String.valueOf(idPenerbit) + " Not Found!"
            );

        } catch (UnsupportedOperationException e) {
            throw new ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Buku is still open!"
            );
        }
    }

}

