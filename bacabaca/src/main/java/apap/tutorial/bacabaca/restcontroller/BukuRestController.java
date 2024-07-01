package apap.tutorial.bacabaca.restcontroller;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.UUID;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import apap.tutorial.bacabaca.dto.BukuMapper;
import apap.tutorial.bacabaca.dto.PenerbitMapper;
import apap.tutorial.bacabaca.dto.request.CreateBukuRequestDTO;
import apap.tutorial.bacabaca.dto.request.UpdateBukuRequestDTO;
import apap.tutorial.bacabaca.dto.response.ReadBukuResponseDTO;
import apap.tutorial.bacabaca.model.Buku;
import apap.tutorial.bacabaca.rest.BukuDetail;
import apap.tutorial.bacabaca.restservice.BukuRestService;
import jakarta.validation.Valid;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
public class BukuRestController {
    
    @Autowired
    private BukuMapper bukuMapper;

    @Autowired
    private BukuRestService bukuRestService;

    @GetMapping(value ="/buku/view-all")
    private List<Buku> retrieveAllBuku(){
        return bukuRestService.retrieveRestAllBuku();
    }

    @GetMapping(value="/buku/{id}")
    private Buku retrieveBuku(@PathVariable("id") String id){
        try{
            return bukuRestService.getRestBukuById(UUID.fromString(id));
        } catch (NoSuchElementException e){
            throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "Id Buku " + id + " not found"
            );
        }
    }

    @PostMapping(value = "/buku/create")
    public Buku restAddBuku(@Valid @RequestBody CreateBukuRequestDTO bukuDTO, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Request body has invalid type or missin field"
            );
        } else{
            var buku = bukuMapper.createBukuRequestDTOToBuku(bukuDTO);
            bukuRestService.createRestBuku(buku);
            return buku;
        }
    }

    @GetMapping(value="/buku/status")
    private Mono<String> getStatus(){
        return bukuRestService.getStatus();
    }
    
    @PostMapping(value="/full")
    private Mono<BukuDetail> postStatus(){
        return bukuRestService.postStatus();
    }


    @PostMapping("/buku/translate")
    public ResponseEntity<Buku> translateBookTitle(@RequestBody UpdateBukuRequestDTO bukuDTO) throws IOException, InterruptedException {
        String apiURL = "https://text-translator2.p.rapidapi.com/translate";

        Buku buku = bukuMapper.updateBukuRequestDTOToBuku(bukuDTO);
        Buku bk1 = bukuRestService.updateRestBuku(buku.getId(), buku.getTargetLanguage(), buku.getSourceLanguage());
        String requestBody = "source_language=" + bk1.getSourceLanguage() + "&target_language=" + bk1.getTargetLanguage() + "&text=" + bk1.getJudul();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(apiURL))
                .header("content-type", "application/x-www-form-urlencoded")
                .header("X-RapidAPI-Key", "05b9d1f33bmshcabaf724ee59e15p1a9c82jsn5d4d86cc25fd")
                .header("X-RapidAPI-Host", "text-translator2.p.rapidapi.com")
                .POST(HttpRequest.BodyPublishers.ofString(requestBody))
                .build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

        if (response.statusCode() == 200) {
            String responseBody = response.body();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(responseBody);
            String translatedText = jsonNode.get("data").get("translatedText").asText();
            bk1.setJudul(translatedText);
            return ResponseEntity.ok().body(bk1);
        } else {
            // Handle unsuccessful response
            return ResponseEntity.status(response.statusCode()).build();
        }
    }

    
    // Tutorial 6                       
    @PutMapping(value = "/buku")
    public Buku restUpdateBuku(@Valid @RequestBody UpdateBukuRequestDTO bukuDTO, BindingResult bindingResult){
        if(bindingResult.hasFieldErrors()){
            throw new ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Request body has invalid type or missin field");
        } else {
            var buku = bukuMapper.updateBukuRequestDTOToBuku(bukuDTO);
            return bukuRestService.updateRestBuku2(buku);
        }
    }

    @GetMapping(value = "buku/search")
    public List<Buku> searchRestBuku(@RequestParam("query") String judul) {
        List<Buku> listBuku = bukuRestService.getRestBukuByJudul(judul);
        return listBuku;
    }

    @GetMapping(value="/random")
    public ResponseEntity random() {
        Random random = new Random();
        var theBool = random.nextBoolean();
        if (theBool){
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.badRequest().build();
    }
    

}
