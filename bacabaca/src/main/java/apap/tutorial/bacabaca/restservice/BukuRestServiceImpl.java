package apap.tutorial.bacabaca.restservice;

import apap.tutorial.bacabaca.model.Buku;
import apap.tutorial.bacabaca.repository.BukuDb;
import apap.tutorial.bacabaca.rest.BukuDetail;
import jakarta.transaction.Transactional;
import reactor.core.publisher.Mono;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
public class BukuRestServiceImpl implements BukuRestService{

    private final String TRANSLATOR_API_URL = "https://text-translator2.p.rapidapi.com/translate";
    private final String API_KEY = "05b9d1f33bmshcabaf724ee59e15p1a9c82jsn5d4d86cc25fd";

    @Autowired
    private RestTemplate restTemplate;
    
    @Autowired
    private BukuDb bukuDb;

    @Override
    public void createRestBuku(Buku buku) { 
        buku.setJudulLower(buku.getJudul().toLowerCase());
        bukuDb.save(buku); 
    };
    
    @Override
    public List<Buku> retrieveRestAllBuku() { 
        return bukuDb.findAllByOrderByJudulLowerAsc(); 
    };

    @Override
    public Buku getRestBukuById(UUID id){
        for(Buku buku : retrieveRestAllBuku()){
            if (buku.getId().equals(id)){
                return buku;
            }
        }
        return null;
    };

    private final String mockUrl = System.getenv("MOCK_URL");

    private final WebClient webClient;

    public BukuRestServiceImpl(WebClient.Builder webClientBuilder){
        this.webClient = webClientBuilder.baseUrl(mockUrl).build(); // mock server
    }

    @Override
    public Mono<String> getStatus(){
        return this.webClient.get().uri("/rest/buku/status").retrieve().bodyToMono(String.class);
    }

    @Override
    public Mono<BukuDetail> postStatus(){
        MultiValueMap<String,String> data = new LinkedMultiValueMap<>();
        data.add("judul", "Buku Cara membaca Jilid 2");
        data.add("tahunTerbit", "2003");
        var response = this.webClient
            .post()
            .uri("/rest/buku/full-status")
            .bodyValue(data)
            .retrieve()
            .bodyToMono(BukuDetail.class);
        return response;
    }

    @Override
    public Buku updateRestBuku(UUID id, String target, String source){
        Buku buku = getRestBukuById(id);
        if (buku != null){
            buku.setTargetLanguage(target);
            buku.setSourceLanguage(source);
            bukuDb.save(buku);
        }
        return buku;
    }

    @Override
    public Buku updateRestBuku1(Buku buku, String judul){
        if (buku != null){
            buku.setJudul(judul);
            bukuDb.save(buku);
        }
        return buku;
    }

    @Override
    public Map<String, Double> getTopBooks() throws IOException, InterruptedException{
        int year = LocalDate.now().getYear();
        int month = LocalDate.now().getMonthValue();
        String url = "https://hapi-books.p.rapidapi.com/month/" + Integer.valueOf(year) + "/" + Integer.valueOf(month);
        HttpRequest request = HttpRequest.newBuilder()
		.uri(URI.create(url))
		.header("X-RapidAPI-Key", "05b9d1f33bmshcabaf724ee59e15p1a9c82jsn5d4d86cc25fd")
		.header("X-RapidAPI-Host", "hapi-books.p.rapidapi.com")
        .header("Content-Type", "application/x-www-form-urlencoded")
		.method("GET", HttpRequest.BodyPublishers.noBody())
		.build();
        HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
        String responseBody = response.body();
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(responseBody);
        
        Map<String, Double> getAllBwithR = new HashMap<>();
        for (int i = 0; i < jsonNode.size(); i++) {
            String name = jsonNode.get(i).get("name").asText();
            Double rating = jsonNode.get(i).get("rating").asDouble();

            getAllBwithR.put(name, rating);
        }
        return getAllBwithR;
        
    }

    // Tutorial 6

    @Override
    public Buku updateRestBuku2(Buku bukuFromDto){
        Buku buku = getRestBukuById(bukuFromDto.getId());
        if (buku != null) {
            buku.setHarga(bukuFromDto.getHarga());
            buku.setJudul(bukuFromDto.getJudul());
            buku.setListPenulis(bukuFromDto.getListPenulis());
            buku.setTahunTerbit(bukuFromDto.getTahunTerbit());
            buku.setPenerbit(bukuFromDto.getPenerbit());
            buku.setJudulLower(bukuFromDto.getJudul().toLowerCase());
            bukuDb.save(buku);
        }
        return buku;
    }


    @Override
    public List<Buku> getRestBukuByJudul(String judul){
        List<Buku> bukuList;
        if (judul != null && !judul.isEmpty()) {
            // Jika ada keyword pencarian, gunakan metode pencarian dengan Derived Query Method
            bukuList = bukuDb.findByJudulContainingIgnoreCase(judul);
        } else {
            // Jika tidak ada keyword pencarian, tampilkan semua buku
            bukuList = bukuDb.findAll();
        }
        return bukuList;
    }

}
    

