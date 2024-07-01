package apap.tutorial.bacabaca.restservice;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import apap.tutorial.bacabaca.model.Penerbit;
import apap.tutorial.bacabaca.repository.PenerbitDb;

public interface PenerbitRestService {

    List<Penerbit> retrieveRestAllPenerbit();

    Penerbit createRestPenerbit(Penerbit penerbit);

    Penerbit putRestPenerbit(Long idPenerbit, Penerbit penerbit);

    Penerbit getRestPenerbitById(Long idPenerbit);

    void deletePenerbit(Long idPenerbit);
}
