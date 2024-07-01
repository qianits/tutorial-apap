package apap.tutorial.bacabaca.restservice;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;

import reactor.core.publisher.Mono;

import apap.tutorial.bacabaca.model.Buku;
import apap.tutorial.bacabaca.rest.BukuDetail;


public interface BukuRestService {
    
    void createRestBuku(Buku buku);

    List<Buku> retrieveRestAllBuku();

    Buku getRestBukuById(UUID id);

    Mono<String> getStatus();

    Mono<BukuDetail> postStatus();

    Buku updateRestBuku(UUID id, String target, String source);

    Buku updateRestBuku1(Buku buku, String judul);

    Buku updateRestBuku2(Buku buku);

    Map<String, Double> getTopBooks()throws IOException, InterruptedException;

    List<Buku> getRestBukuByJudul(String judul);
}
