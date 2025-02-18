package apap.tutorial.bacabaca.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import apap.tutorial.bacabaca.model.Penerbit;
import apap.tutorial.bacabaca.repository.PenerbitDb;

@Service
public class PenerbitServiceImpl implements PenerbitService{

    @Autowired
    PenerbitDb penerbitDb;

    @Override
    public Penerbit createPenerbit(Penerbit penerbit) { return penerbitDb.save(penerbit); }

    @Override
    public List<Penerbit> getAllPenerbit() { return penerbitDb.findAll();}

    @Override
    public Penerbit getPenerbitById(Long idPenerbit){
        return penerbitDb.findById(idPenerbit).get();
    }

    @Override
    public Map<String, Integer> getPublisherBookCounts(){
        List<Penerbit> listPenerbit = getAllPenerbit();
        Map<String, Integer> publisherBookCounts = new HashMap<>();

        for (Penerbit penerbit : listPenerbit) {
            String publisherName = penerbit.getNamaPenerbit();
            int bookCount = penerbit.getListBuku().size();

            publisherBookCounts.put(publisherName, bookCount);
        }
        return publisherBookCounts;
    }
}
