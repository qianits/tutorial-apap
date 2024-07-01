package apap.tutorial.bacabaca.service;

import java.util.List;
import java.util.UUID;

import apap.tutorial.bacabaca.dto.BukuMapper;
import apap.tutorial.bacabaca.repository.BukuDb;
import jakarta.websocket.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import apap.tutorial.bacabaca.model.Buku;

@Service
public class BukuServiceImpl implements BukuService{
    
    @Autowired
    BukuDb bukuDb;

    @Override
    public void saveBuku(Buku buku){
        buku.setJudulLower(buku.getJudul().toLowerCase());
        bukuDb.save(buku);
    }

    @Override
    public List<Buku> getAllBuku() { 
        var listNoDeleted = bukuDb.findAllByOrderByJudulLowerAsc();
        for (int i = 0; i < listNoDeleted.size(); i++){
            if(listNoDeleted.get(i).isDeleted()){
                listNoDeleted.remove(i);
            }
        }
        return listNoDeleted; 
        
    }
    
    @Override
    public Buku getBukuById(UUID kodeBuku){
        for (Buku buku : getAllBuku()){
            if(buku.getId().equals(kodeBuku)){
                return buku;
            }
        }
        return null;
    }

    @Override
    public Buku updateBuku(Buku bukuFromDto){
        Buku buku = getBukuById(bukuFromDto.getId());
        if (buku != null){
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
    public boolean isJudulExist(String judul){
        return getAllBuku().stream().anyMatch(b -> b.getJudul().equals(judul));
    }

    @Override
    public boolean isJudulExist(String judul, UUID id){
        return getAllBuku().stream().anyMatch(b -> b.getJudul().equals(judul) && !b.getId().equals(id));
    }

    @Override
    public void deleteBuku(Buku buku){
        bukuDb.delete(buku);
    }

    @Override
    public void createBuku(Buku buku) {
        bukuDb.save(buku);
    }

    @Override
    public List<Buku> searchBuku(String judul){
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