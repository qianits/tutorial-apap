package apap.tutorial.bacabaca.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import apap.tutorial.bacabaca.dto.request.CreatePenulisRequestDTO;
import apap.tutorial.bacabaca.dto.request.DeleteMultiplePenulisDTO;
import apap.tutorial.bacabaca.model.Sertifikasi;

import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import apap.tutorial.bacabaca.dto.PenulisMapper;
import apap.tutorial.bacabaca.service.PenulisService;

@Controller
public class PenulisController {
    
    @Autowired
    PenulisService penulisService;

    @Autowired
    PenulisMapper penulisMapper;

    @GetMapping("penulis/create")
    public String formAddPenulis(Model model){

        //Membuat DTO untuk dikirim ke view.
        var penulisDTO = new CreatePenulisRequestDTO();

        //Menambah penerbitDTO ke model thymeleaf
        model.addAttribute("penulisDTO", penulisDTO);

        return "form-create-penulis";
    }

    @PostMapping("penulis/create")
    public String addPenulis(@ModelAttribute CreatePenulisRequestDTO createPenulisRequestDTO, Model model){

        // Dengan bantuan mapper, buat object Penulis dengan data yang berasal dari DTO.
        var penulis = penulisMapper.createPenulisRequestDTOToPenulis(createPenulisRequestDTO);
        
        if (penulis.getListSertifikasi() != null){
            for (Sertifikasi sertifikasi : penulis.getListSertifikasi()){
                sertifikasi.setPenulis(penulis);
            }
        }

        //Memanggil Service createPenerbit
        penulisService.createPenulis(penulis);

        //Menambah penerbit ke model thymeleaf
        model.addAttribute("penulis", createPenulisRequestDTO);

        return "success-create-penulis";
    }

    @GetMapping("penulis/viewall")
    public String listPenuliString(Model model){
        var listPenulis = penulisService.getAllPenulis();
        var deleteDTO = new DeleteMultiplePenulisDTO();
        //Menambah penerbitDTO ke model thymeleaf
        model.addAttribute("listPenulis", listPenulis);
        model.addAttribute("deleteDTO", deleteDTO);
        return "viewall-penulis";
    }

    @PostMapping("penulis/delete")
    public String deleteMultiplePenulis(@ModelAttribute DeleteMultiplePenulisDTO deleteDTO){
        penulisService.deleteListPenulis(deleteDTO.getListPenulis());
        return "success-delete-penulis";
    }

    @PostMapping(value = "penulis/create", params ={"addRowSertifikasi"})
    public String addRowSertifikasiPenulis(@ModelAttribute CreatePenulisRequestDTO penulisDTO, Model model){

        if(penulisDTO.getListSertifikasi() == null || penulisDTO.getListSertifikasi().size() == 0){
            penulisDTO.setListSertifikasi(new ArrayList<>());
        }

        // Memasukan Sertifikasi baru (kosong) ke list, untuk dirender sebagai row baru.
        penulisDTO.getListSertifikasi().add(new Sertifikasi());

        model.addAttribute("penulisDTO", penulisDTO);
        return "form-create-penulis";
    }

    @PostMapping(value = "penulis/create", params ={"deleteRowSertifikasi"})
    public String deleteRowSertifikasiPEnulis(@ModelAttribute CreatePenulisRequestDTO penulisDTO, @RequestParam("deleteRowSertifikasi") int row, Model model){

        penulisDTO.getListSertifikasi().remove(row);

        model.addAttribute("penulisDTO", penulisDTO);
        return "form-create-penulis";
    }

}
