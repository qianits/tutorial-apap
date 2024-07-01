package apap.tutorial.bacabaca.service;

import java.util.List;
import java.util.Map;

import apap.tutorial.bacabaca.model.Penerbit;

public interface PenerbitService {
    Penerbit createPenerbit(Penerbit penerbit);
    List<Penerbit> getAllPenerbit();
    Penerbit getPenerbitById(Long idPenerbit);
    Map<String, Integer> getPublisherBookCounts();
}
