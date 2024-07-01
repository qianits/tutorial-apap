package apap.tutorial.bacabaca.controller;

import apap.tutorial.bacabaca.dto.BukuMapper;
import apap.tutorial.bacabaca.dto.request.CreateBukuRequestDTO;
import apap.tutorial.bacabaca.dto.request.UpdateBukuRequestDTO;
import apap.tutorial.bacabaca.model.Buku;
import apap.tutorial.bacabaca.model.Penulis;
import apap.tutorial.bacabaca.repository.BukuDb;
import apap.tutorial.bacabaca.restservice.BukuRestService;
import apap.tutorial.bacabaca.service.BukuService;
import apap.tutorial.bacabaca.service.PenerbitService;
import apap.tutorial.bacabaca.service.PenulisService;
import jakarta.validation.Valid;

import org.mapstruct.AfterMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.boot.autoconfigure.web.ServerProperties;


@Controller
public class BukuController{

    @Autowired
    private BukuMapper bukuMapper;

    @Autowired
    private BukuService bukuService;

    @Autowired
    private PenerbitService penerbitService;

    @Autowired
    private PenulisService penulisService;

    @Autowired
    private BukuRestService bukuRestService;

    @GetMapping("/")
    public String home(){
        return "home";
    }

    @GetMapping("buku/create")
    public String formAddBuku(Model model){
        // Kirim bukuDTO untuk nantinya isian field di-bind ke sini.
        var bukuDTO = new CreateBukuRequestDTO();
        model.addAttribute("bukuDTO", bukuDTO);
        model.addAttribute("listPenerbit", penerbitService.getAllPenerbit());

        // Kirim list penerbit untuk menjadi pilihan pada dropdown.
        var listPenerbit = penerbitService.getAllPenerbit();
        model.addAttribute("listPenerbit", listPenerbit);

        // Kirim list penerbit untuk menjadi pilihan pada dropdown.
        var listPenulisExisting = penulisService.getAllPenulis();
        model.addAttribute("listPenulisExisting", listPenulisExisting);

        return "form-create-buku";
    }

    @PostMapping("buku/create")
    public String addBuku(@Valid @ModelAttribute CreateBukuRequestDTO bukuDTO, BindingResult bindingResult, Model model){
        if (bindingResult.hasErrors()) {
            
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (ObjectError error : errors) {
                errorMessage.append(error.getDefaultMessage()).append("\n");
            }
            model.addAttribute("errorMessage", errorMessage.toString());
            return "error-view";
        }

        //Cek judul apakah sudah ada atau belum
        if (bukuService.isJudulExist(bukuDTO.getJudul())){
            var errorMessage = "Maaf, judul buku sudah ada";
            model.addAttribute("errorMessage", errorMessage);
            return "error-view";
        }

        var buku = bukuMapper.createBukuRequestDTOToBuku(bukuDTO);


        //Memanggil Service addBuku
        bukuService.saveBuku(buku);

        //Add variabel id buku ke 'id' untuk dirender di thymeleaf
        model.addAttribute("id",buku.getId());

        //Add variabel judul ke 'judul' untuk dirender di thymeleaf
        model.addAttribute("judul", buku.getJudul());

        return "success-create-buku";
    }

    @PostMapping(value = "buku/create", params ={"addRow"})
    public String addRowPenulisBuku(@ModelAttribute CreateBukuRequestDTO createBukuRequestDTO, Model model){

        if(createBukuRequestDTO.getListPenulis() == null || createBukuRequestDTO.getListPenulis().size() == 0){
            createBukuRequestDTO.setListPenulis(new ArrayList<>());
        }

        // Memasukan Penulis baru (kosong) ke list, untuk dirender sebagai row baru.
        createBukuRequestDTO.getListPenulis().add(new Penulis());

        // Kirim list penerbit penulis untuk menjadi pilihan pada dropdown.
        model.addAttribute("listPenulisExisting", penulisService.getAllPenulis());
        model.addAttribute("listPenerbit", penerbitService.getAllPenerbit());

        model.addAttribute("bukuDTO", createBukuRequestDTO);
        return "form-create-buku";
    }

    @PostMapping(value = "buku/update", params ={"addRowUpdate"})
    public String addRowPenulisBukuUpdate( @ModelAttribute UpdateBukuRequestDTO bukuDTO, Model model){

        if(bukuDTO.getListPenulis() == null || bukuDTO.getListPenulis().size() == 0){
            bukuDTO.setListPenulis(new ArrayList<>());
        }

        // Memasukan Penulis baru (kosong) ke list, untuk dirender sebagai row baru.
        bukuDTO.getListPenulis().add(new Penulis());

        // Kirim list penerbit penulis untuk menjadi pilihan pada dropdown.
        model.addAttribute("listPenulisExisting", penulisService.getAllPenulis());
        model.addAttribute("listPenerbit", penerbitService.getAllPenerbit());

        model.addAttribute("bukuDTO", bukuDTO);
        return "form-update-buku";
    }

    @PostMapping(value = "buku/create", params ={"deleteRow"})
    public String deleteRowPenulisBuku(@ModelAttribute CreateBukuRequestDTO createBukuRequestDTO, @RequestParam("deleteRow") int row, Model model){

        createBukuRequestDTO.getListPenulis().remove(row);
        model.addAttribute("bukuDTO", createBukuRequestDTO);

        model.addAttribute("listPenulisExisting", penulisService.getAllPenulis());
        model.addAttribute("listPenerbit", penerbitService.getAllPenerbit());
        return "form-create-buku";
    }

    @PostMapping(value = "buku/update", params ={"deleteRowUpdate"})
    public String deleteRowPenulisBukuUpdate(@ModelAttribute UpdateBukuRequestDTO bukuDTO, @RequestParam("deleteRowUpdate") int row, Model model){

        bukuDTO.getListPenulis().remove(row);
        model.addAttribute("bukuDTO", bukuDTO);

        model.addAttribute("listPenulisExisting", penulisService.getAllPenulis());
        model.addAttribute("listPenerbit", penerbitService.getAllPenerbit());
        return "form-update-buku";
    }

    @GetMapping("buku/viewall")
    public String listBuku(Model model){
        //Mendapatkan semua buku
        List<Buku> listBuku = bukuService.getAllBuku();

        //Add variabel semua bukuModel ke "ListBuku" untuk dirender pada thymeleaf
        model.addAttribute("listBuku", listBuku);
        return "viewall-buku";
    }

    @GetMapping(value = "/buku/{id}")
    public String detailBuku(@PathVariable(value = "id") UUID id, Model model){
        //Mendapatkan buku melalui kodeBuku
        var buku = bukuService.getBukuById(id);
        var bukuDTO = bukuMapper.readBukuResponseDTO(buku);
        model.addAttribute("buku",bukuDTO);
        return "view-buku";
    }


    @GetMapping("buku/{id}/update")
    public String formUpdateBuku(@PathVariable("id") UUID id, Model model){
    
        //Mengambil buku dengan id tersebut
        var buku = bukuService.getBukuById(id);
    
        //Memindahkan data buku ke DTO untuk selanjutnya diubah di form pengguna
        var bukuDTO = bukuMapper.bukuToUpdateBukuRequestDTO(buku);

    
        model.addAttribute("bukuDTO", bukuDTO);
        model.addAttribute("listPenerbit", penerbitService.getAllPenerbit());
        model.addAttribute("listPenulisExisting", penulisService.getAllPenulis());
        return "form-update-buku";
    }

    @PostMapping("buku/update")
    public String updateBuku(@Valid @ModelAttribute UpdateBukuRequestDTO bukuDTO, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            
            List<ObjectError> errors = bindingResult.getAllErrors();
            StringBuilder errorMessage = new StringBuilder();
            for (ObjectError error : errors) {
                errorMessage.append(error.getDefaultMessage()).append("<br>");
            }
            model.addAttribute("errorMessage", errorMessage.toString());
            return "error-view";
        }
        
        if (bukuService.isJudulExist(bukuDTO.getJudul(), bukuDTO.getId())){
            var errorMessage = "Maaf, judul buku sudah ada";
            model.addAttribute("errorMessage", errorMessage);
            return "error-view";
        }

        var bukuFromDto = bukuMapper.updateBukuRequestDTOToBuku(bukuDTO);

        //Memanggil Service addBuku
        var buku = bukuService.updateBuku(bukuFromDto);

        //Add variabel id buku ke 'id' untuk dirender di thymeleaf
        model.addAttribute("id",buku.getId());

        //Add variabel judul ke 'judul' untuk dirender di thymeleaf
        model.addAttribute("judul", buku.getJudul());

        return "success-update-buku";
    }

    @GetMapping(value = "/buku/{id}/delete")
    public String deleteBuku(@PathVariable(value = "id") UUID id, Model model){
        
        var buku = bukuService.getBukuById(id);
        bukuService.deleteBuku(buku);
        model.addAttribute("id",id);
        return "success-delete-buku";
    }


    @GetMapping("/search")
    public String searchBuku(@RequestParam(value = "searchQuery", required = false) String searchQuery, Model model) {
        // Jika searchQuery tidak ada atau kosong, tampilkan semua buku
        List<Buku> hasilPencarian;
        if (searchQuery == null || searchQuery.isEmpty()) {
            hasilPencarian = bukuService.getAllBuku();
        }

        // Lakukan pencarian berdasarkan judul buku (case insensitive)
        hasilPencarian = bukuService.searchBuku(searchQuery);

        // Tambahkan hasil pencarian ke model
        model.addAttribute("listBuku", hasilPencarian);

        return "viewall-buku";
    }

    @GetMapping("buku/viewall-with-datatables")
    public String listBukuDT(Model model){
        //Mendapatkan semua buku
        List<Buku> listBuku = bukuService.getAllBuku();

        //Add variabel semua bukuModel ke "ListBuku" untuk dirender pada thymeleaf
        model.addAttribute("listBuku", listBuku);
        return "viewall-with-datatables";
    }

    @GetMapping("/buku/chart")
    public String getTopBooksInMonth(Model model) throws IOException, InterruptedException {

        var listBukuPopuler = bukuRestService.getTopBooks();
        model.addAttribute("listBukuPopuler", listBukuPopuler);

        return "chart";
    }

    @Autowired
    ServerProperties serverProperties;

    @RequestMapping("port")
    public String ActivePort(Model model){
        model.addAttribute("port", serverProperties.getPort());
        return "active-port";
    }


}
