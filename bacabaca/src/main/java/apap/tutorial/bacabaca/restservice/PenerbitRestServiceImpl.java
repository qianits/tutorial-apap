package apap.tutorial.bacabaca.restservice;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.tutorial.bacabaca.model.Penerbit;
import apap.tutorial.bacabaca.repository.PenerbitDb;
import jakarta.transaction.Transactional;

@Service
@Transactional
public class PenerbitRestServiceImpl implements PenerbitRestService{
    
    @Autowired
    private PenerbitDb penerbitDb;

    @Override
    public List<Penerbit> retrieveRestAllPenerbit(){
        return penerbitDb.findAll();
    }

    @Override
    public Penerbit createRestPenerbit(Penerbit penerbit){
        return penerbitDb.save(penerbit);
    }

    @Override
    public Penerbit putRestPenerbit(Long idPenerbit, Penerbit penerbit){
        Optional<Penerbit> penerbitini = penerbitDb.findById(idPenerbit);
        
        Penerbit penerbitBaru = penerbitini.get();

        penerbitBaru.setAlamat(penerbit.getAlamat());
        penerbitBaru.setEmail(penerbit.getEmail());
        penerbitBaru.setNamaPenerbit(penerbit.getNamaPenerbit());

        return penerbitBaru;
    }

    @Override
    public Penerbit getRestPenerbitById(Long id){
        for(Penerbit penerbit : retrieveRestAllPenerbit()){
            if (penerbit.getIdPenerbit() == id){
                return penerbit;
            }
        }
        return null;
    }

    @Override
    public void deletePenerbit(Long idPenerbit){
        Penerbit penerbit = penerbitDb.findById(idPenerbit).get();
        penerbitDb.delete(penerbit);
    }
}
